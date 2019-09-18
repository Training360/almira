package crystalball.service;

import crystalball.entities.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    private MessageSource messageSource;

    @Value("${file.location}")
    private Path dir;

    @Value("${skip.authentication}")
    private boolean skipAuthentication;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public OperationResult<MessageDetailsDto> saveMessage(CreateMessageCommand command, String token) {
        log.debug("Create message: {}, {}", command.getContent(), command.getCreator());
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setContent(command.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message.setOpenAt(command.getOpenAt());
        message.setCreator(command.getCreator());
        message.setToken(token);
        messages.add(message);
        return new OperationResult(OperationStatus.OK, messageSource.getMessage("message.created",
                new Object[]{},
                LocaleContextHolder.getLocale()), toMessageDetailsDto(message));
    }

    public List<MessageDto> listMessages() {
        log.debug("List messages");
        return messages.stream().map(this::toMessageDto).collect(Collectors.toList());
    }

    public MessageDetailsDto getMessageById(String id, String token, Optional<LocalDateTime> timeMachine) {
        log.debug("Get message by id: {}", id);
        return messages.stream().filter(m -> m.getId().equals(id)).map(m -> toHashedMessageDetailsDto(m, token, timeMachine)).findFirst()
                .orElseThrow(() -> new IllegalRequestException(OperationStatus.NOT_FOUND, messageSource.getMessage("message.not.found",
                        new Object[]{},
                        LocaleContextHolder.getLocale())));
    }

    public OperationResult updateMessage(String id, UpdateMessageCommand command, String token) {
        log.debug("Update message by id: {}, {}", id, command.getContent());
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (message.isEmpty()) {
            throw new IllegalRequestException(OperationStatus.NOT_FOUND, messageSource.getMessage("message.not.found",
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        else {
            checkToken(token, message.get());
            message.get().setContent(command.getContent());
            return new OperationResult(OperationStatus.OK, messageSource.getMessage("message.updated",
                    new Object[]{},
                    LocaleContextHolder.getLocale()), toMessageDetailsDto(message.get()));
        }
    }

    public OperationResult delete(String id, String token) {
        log.debug("Delete message by id: {}", id);
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst();

        checkToken(token, message.get());

        if (message.isEmpty()) {
            throw new IllegalRequestException(OperationStatus.NOT_FOUND, messageSource.getMessage("message.not.found",
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        else {
            messages.remove(message.get());
            return new OperationResult(OperationStatus.OK, messageSource.getMessage("message.has.deleted",
                    new Object[]{},
                    LocaleContextHolder.getLocale()), toMessageDetailsDto(message.get()));
        }
    }

    public OperationResult uploadFile(String id, MultipartFile file, String token) {
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalRequestException(OperationStatus.NOT_FOUND, messageSource.getMessage("message.not.found",
                        new Object[]{},
                        LocaleContextHolder.getLocale())));
        checkToken(token, message);
        String filename = id + getExtension(file.getOriginalFilename());
        try {
            file.transferTo(dir.resolve(filename));
            message.setFilename(filename);
            return new OperationResult(OperationStatus.OK, messageSource.getMessage("file.has.uploaded",
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("Can not transfer file", ioe);
        }
    }

    public FileResult getFile(String id) {
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalRequestException(OperationStatus.NOT_FOUND, messageSource.getMessage("message.not.found",
                        new Object[]{},
                        LocaleContextHolder.getLocale())));
        String filename = message.getFilename();
        if (filename == null) {
            throw new IllegalRequestException(OperationStatus.NOT_FOUND,  messageSource.getMessage("message.not.found",
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
        Resource resource = new FileSystemResource(dir.resolve(filename));
        var contentType = "";
        try {
            contentType = Files.probeContentType(dir.resolve(filename));
        }
        catch (IOException ioe) {
            log.error("Cannot determine content type");
        }
        return new FileResult(filename, resource, contentType);
    }

    private String getExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    private boolean isValidToken(String token, Message message) {
        return token != null && !token.isBlank() && token.equals(message.getToken());
    }

    public void checkToken(String token, Message message) {
        if (!skipAuthentication && !isValidToken(token, message)) {
            throw new IllegalRequestException(OperationStatus.UNAUTHORIZED,  messageSource.getMessage("unauthorized",
                    new Object[]{},
                    LocaleContextHolder.getLocale()));
        }
    }

    public MessageDto toMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setCreatedAt(message.getCreatedAt());
        messageDto.setCreator(message.getCreator());
        return messageDto;
    }

    public MessageDetailsDto toHashedMessageDetailsDto(Message message, String token, Optional<LocalDateTime> timeMachine) {
        var dto = toMessageDetailsDto(message);

        LocalDateTime base = timeMachine.orElse(LocalDateTime.now());

        // Van autentikáció és valid, vagy lejárt
        if (!skipAuthentication && (!isValidToken(token, message)) &&
                !message.getOpenAt().isBefore(base.plus(5, ChronoUnit.MINUTES))) {
            dto.setContent(toMd5(message.getContent()));
        }

        // Nincs autentikáció
        if (skipAuthentication && !message.getOpenAt().isBefore(base.plus(5, ChronoUnit.MINUTES))) {
            dto.setContent(toMd5(message.getContent()));
        }

        return dto;
    }

    public static MessageDetailsDto toMessageDetailsDto(Message message) {
        MessageDetailsDto messageDto = new MessageDetailsDto();
        messageDto.setId(message.getId());
        messageDto.setCreatedAt(message.getCreatedAt());
        messageDto.setCreator(message.getCreator());
        messageDto.setOpenAt(message.getOpenAt());
        messageDto.setContent(message.getContent());
        messageDto.setFilename(message.getFilename());
        return messageDto;
    }

    private static String toMd5(String value) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte[] digest = md.digest();
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Can not hash", e);
        }
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}

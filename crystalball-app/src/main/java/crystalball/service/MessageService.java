package crystalball.service;

import crystalball.entities.Message;
import crystalball.service.CreateMessageCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    public OperationResult saveMessage(CreateMessageCommand command) {
        log.debug("Create message: {}, {}", command.getContent(), command.getCreator());
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setContent(command.getContent());
        message.setCreatedAt(LocalDateTime.now());
        message.setOpenAt(command.getOpenAt());
        message.setCreator(command.getCreator());
        messages.add(message);
        return new OperationResult(OperationResult.Status.OK, "Message has created", toMessageDetailsDto(message));
    }

    public List<MessageDto> listMessages() {
        log.debug("List messages");
        return messages.stream().map(MessageService::toMessageDto).collect(Collectors.toList());
    }

    public Optional<MessageDetailsDto> getMessageById(String id) {
        log.debug("Get message by id: {}", id);
        return messages.stream().filter(m -> m.getId().equals(id)).map(MessageService::toMessageDetailsDto).findFirst();
    }

    public OperationResult updateMessage(String id, UpdateMessageCommand command) {
        log.debug("Update message by id: {}, {}", id, command.getContent());
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (message.isEmpty()) {
            return new OperationResult(OperationResult.Status.NOT_FOUND, "Message not found");
        }
        else {
            message.get().setContent(command.getContent());
            return new OperationResult(OperationResult.Status.OK, "Message has updated", toMessageDetailsDto(message.get()));
        }
    }

    public OperationResult delete(String id) {
        log.debug("Delete message by id: {}", id);
        var message = messages.stream().filter(m -> m.getId().equals(id)).findFirst();
        if (message.isEmpty()) {
            return new OperationResult(OperationResult.Status.NOT_FOUND, "Message not found");
        }
        else {
            messages.remove(message.get());
            return new OperationResult(OperationResult.Status.OK, "Message has deleted", toMessageDetailsDto(message.get()));
        }
    }

    public static MessageDto toMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setCreatedAt(message.getCreatedAt());
        messageDto.setCreator(message.getCreator());
        return messageDto;
    }

    public static MessageDetailsDto toMessageDetailsDto(Message message) {
        MessageDetailsDto messageDto = new MessageDetailsDto();
        messageDto.setId(message.getId());
        messageDto.setCreatedAt(message.getCreatedAt());
        messageDto.setCreator(message.getCreator());
        messageDto.setOpenAt(message.getOpenAt());
        if (message.getOpenAt().isBefore(LocalDateTime.now().plus(5, ChronoUnit.MINUTES))) {
            messageDto.setContent(message.getContent());
        }
        else {
            messageDto.setContent(toMd5(message.getContent()));
        }
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

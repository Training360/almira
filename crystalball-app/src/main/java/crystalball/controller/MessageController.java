package crystalball.controller;

import crystalball.service.*;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    private MessageSource messageSource;

    public MessageController(MessageService messageService, MessageSource messageSource) {
        this.messageService = messageService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<OperationResult<MessageDetailsDto>> saveMessage(@Valid @RequestBody CreateMessageCommand command) {
        var token = messageService.generateToken();
        var result =  messageService.saveMessage(command, token);
        return ResponseEntity
                .ok()
                .header("token", token)
                .body(result);
    }

    @GetMapping
    public List<MessageDto> listMessages() {
        return messageService.listMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDetailsDto> getMessageById(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        String token = getToken(headers);
        var message = messageService.getMessageById(id, token, getTimeMachine(headers));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private Optional<LocalDateTime> getTimeMachine(Map<String, String> headers) {
        String value = headers.get("time-machine");
        if (value == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return Optional.of(LocalDateTime.parse(value, formatter));
    }

    @PostMapping("/{id}")
    public ResponseEntity<OperationResult> updateMessage(@PathVariable String id, @Valid @RequestBody UpdateMessageCommand command, @RequestHeader Map<String, String> headers) {
        String token = getToken(headers);
        var result = messageService.updateMessage(id, command, token);

        return ResponseEntity
                .status(resolveStatus(result.getStatus()))
                .body(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<OperationResult> delete(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        String token = getToken(headers);
        var result = messageService.delete(id, token);

        return ResponseEntity
                .status(resolveStatus(result.getStatus()))
                .body(result);
    }


    @PostMapping("/{id}/file")
    public ResponseEntity<OperationResult> handleFileUpload(@PathVariable String id,
                                                            @RequestParam("file") MultipartFile file,
                                                            @RequestHeader Map<String, String> headers) {
        String token = getToken(headers);
        var result = messageService.uploadFile(id, file, token);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> handleFileDownload(@PathVariable String id) {
        var result = messageService.getFile(id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, result.getContentType())
                .header( HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + result.getFilename() + "\"").body(result.getResource());
    }

    public String getToken(Map<String, String> headers) {
        String header = headers.get("authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return "";
        }
        return header.substring("Bearer ".length()).trim();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public OperationResult handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst().get();
        String message = "";
        if (error instanceof FieldError) {
            message += ((FieldError)error).getField() + " - ";
        }
        message += error.getDefaultMessage();
        return new OperationResult(OperationStatus.VALIDATION_FAILED, message);
    }

    @ExceptionHandler(IllegalRequestException.class)
    public ResponseEntity<OperationResult> handleIllegalRequest(IllegalRequestException ex) {
        ResponseEntity.BodyBuilder
                response = ResponseEntity
                .status(resolveStatus(ex.getOperationStatus()));

        if (ex.getOperationStatus() == OperationStatus.VALIDATION_FAILED) {
            response.header("WWW-Authenticate", "Bearer");
        }

        return response
                .body(new OperationResult(ex.getOperationStatus(), ex.getMessage()));
    }

    private HttpStatus resolveStatus(OperationStatus status) {
        if (status == OperationStatus.OK) {
            return HttpStatus.OK;
        }
        else if (status == OperationStatus.NOT_FOUND) {
            return HttpStatus.NOT_FOUND;
        }
        else if (status == OperationStatus.VALIDATION_FAILED) {
            return HttpStatus.BAD_REQUEST;
        }
        else if (status == OperationStatus.UNAUTHORIZED) {
            return HttpStatus.UNAUTHORIZED;
        }
        else {
            throw new IllegalArgumentException("Status not found " + status);
        }
    }
}

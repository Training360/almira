package crystalball.controller;

import crystalball.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> saveMessage(@Valid @RequestBody CreateMessageCommand command) {
        var result =  messageService.saveMessage(command);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<MessageDto> listMessages() {
        return messageService.listMessages();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getMessageById(@PathVariable String id) {
        var message = messageService.getMessageById(id);
        if (message.isEmpty()) {
            return new ResponseEntity<>(new OperationResult(OperationResult.Status.NOT_FOUND, "Message not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updateMessage(@PathVariable String id, @Valid @RequestBody UpdateMessageCommand command) {
        var result = messageService.updateMessage(id, command);

        return new ResponseEntity(result, resolveStatus(result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OperationResult> delete(@PathVariable String id) {
        var result = messageService.delete(id);
        return new ResponseEntity(result, resolveStatus(result));
    }

    private HttpStatus resolveStatus(OperationResult result) {
        if (result.getStatus() == OperationResult.Status.OK) {
            return HttpStatus.OK;
        }
        else if (result.getStatus() == OperationResult.Status.NOT_FOUND) {
            return HttpStatus.NOT_FOUND;
        }
        else {
            throw new IllegalArgumentException("Status not found " + result.getStatus());
        }
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
        return new OperationResult(OperationResult.Status.VALIDATION_FAILED, message);
    }
}

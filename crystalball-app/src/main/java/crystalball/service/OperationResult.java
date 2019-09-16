package crystalball.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationResult {

    public enum Status {OK, NOT_FOUND, VALIDATION_FAILED};

    private Status status;

    private String message;

    private MessageDetailsDto messageDetailsDto;

    public OperationResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }
}

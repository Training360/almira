package crystalball.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class IllegalRequestException extends RuntimeException {

    @Getter
    private OperationStatus operationStatus;

    @Getter
    private String message;
}

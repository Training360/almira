package crystalball.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationResult<T> {

    private OperationStatus status;

    private String message;

    private T entity;

    public OperationResult(OperationStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

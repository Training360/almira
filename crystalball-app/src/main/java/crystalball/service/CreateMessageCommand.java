package crystalball.service;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CreateMessageCommand {

    @NotEmpty
    private String content;

    @Future
    private LocalDateTime openAt;

    @NotEmpty
    private String creator;
}

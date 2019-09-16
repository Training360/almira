package crystalball.service;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateMessageCommand {

    @NotEmpty
    private String content;

}

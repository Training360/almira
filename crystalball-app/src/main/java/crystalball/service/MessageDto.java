package crystalball.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private String id;

    private String creator;

    private LocalDateTime createdAt;
}

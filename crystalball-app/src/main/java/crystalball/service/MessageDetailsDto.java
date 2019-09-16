package crystalball.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDetailsDto {

    private String id;

    private String creator;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime openAt;
}

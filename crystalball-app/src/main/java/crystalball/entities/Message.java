package crystalball.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {

    private String id;

    private String creator;

    private String content;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime openAt;

    private String filename;
}

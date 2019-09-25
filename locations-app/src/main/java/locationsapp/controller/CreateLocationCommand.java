package locationsapp.controller;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateLocationCommand {

    private String name;

    private String coords;

    private LocalDateTime interestingAt;

    private String tags;

}

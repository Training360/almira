package locationsapp.controller;

import lombok.Data;

@Data
public class CreateLocationRequest {

    private String name;

    private String coords;

}

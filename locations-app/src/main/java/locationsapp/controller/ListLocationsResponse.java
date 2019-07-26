package locationsapp.controller;

import locationsapp.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListLocationsResponse {

    private List<Location> locations;

    private int start;

    private int count;

}

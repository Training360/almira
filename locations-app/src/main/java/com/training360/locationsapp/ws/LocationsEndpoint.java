package com.training360.locationsapp.ws;

import com.training360.locationsapp.entities.Location;
import com.training360.locationsapp.service.LocationsService;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.List;

@WebService
@Service
public class LocationsEndpoint {

    private LocationsService locationsService;

    public LocationsEndpoint(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    public List<Location> listLocations() {
        return locationsService.listLocations();
    }

    // TODO: Create, Update, Delete
}

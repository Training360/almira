package com.training360.locationsapp.controller;

import com.training360.locationsapp.entities.Location;
import com.training360.locationsapp.service.LocationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@RestController
public class LocationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationsController.class);

    private LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.GET)
    public ListLocationsResponse listLocations(@RequestParam(required = false) Integer start,
                                        @RequestParam(required = false) Integer size) {
        if (start == null) {
            start = 0;
        }
        if (size == null) {
            size = 100_000;
        }

        return locationsService.listLocations(start, size);
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.GET)
    public Location getLocationById(@PathVariable long id) {
        return locationsService.getLocationById(id);
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.DELETE)
    public DeleteLocationResponse deleteLocation(@PathVariable long id) {
        int i = locationsService.deleteLocation(id);
        return new DeleteLocationResponse(i == 1);
    }

    private Optional<DataError> checkValues(String name, String coords) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.of(new DataError("Name can not be empty!"));
        }
        if (!coords.matches("^-?\\d*\\.?\\d*,-?\\d*\\.?\\d*$")) {
            return Optional.of(new DataError("Invalid coordinates format!"));
        }

        try {
            Scanner scanner = new Scanner(coords).useDelimiter(",")
                    .useLocale(Locale.UK);
            double lat = scanner.nextDouble();
            double lon = scanner.nextDouble();
            if (lat < -90 || lat > 90) {
                return Optional.of(new DataError("Latitude must be between -90 and 90"));
            }
            if (lon < -180 || lat > 180) {
                return Optional.of(new DataError("Longitude must be between -180 and 180"));
            }
        } catch (Exception e) {
            return Optional.of(new DataError("Invalid coordinates format!"));
        }
        return Optional.empty();
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> updateLocation(@PathVariable long id, @RequestBody CreateLocationRequest req) {
        Optional<DataError> error = checkValues(req.getName(), req.getCoords());
       if (error.isPresent()) {
           return new ResponseEntity<>(error.get(), HttpStatus.BAD_REQUEST);
       }

        Scanner scanner = new Scanner(req.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();
        Location location = locationsService.updateLocation(id, req.getName(), lat, lon);
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.POST)
    public ResponseEntity<Object> createLocation(@RequestBody CreateLocationRequest req) {
        Optional<DataError> error = checkValues(req.getName(), req.getCoords());
        if (error.isPresent()) {
            return new ResponseEntity<>(error.get(), HttpStatus.BAD_REQUEST);
        }

        Scanner scanner = new Scanner(req.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();
        Location location = locationsService.createLocation(req.getName(), lat, lon);
        return new ResponseEntity(location, HttpStatus.OK);
    }
}

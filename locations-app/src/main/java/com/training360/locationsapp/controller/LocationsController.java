package com.training360.locationsapp.controller;

import com.training360.locationsapp.entities.Location;
import com.training360.locationsapp.service.LocationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private void checkValues(String name, String coords, List<String> errors) {
        if (!coords.matches("^-?\\d*\\.?\\d*,-?\\d*\\.?\\d*$")) {
            errors.add("Invalid coordinates format!");
        }

        try {
            Scanner scanner = new Scanner(coords).useDelimiter(",")
                    .useLocale(Locale.UK);
            double lat = scanner.nextDouble();
            double lon = scanner.nextDouble();

            new Validator().validate(name, lat, lon, errors);
        } catch (Exception e) {
            errors.add("Invalid coordinates format!");
        }
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> updateLocation(@PathVariable long id, @RequestBody CreateLocationRequest req) {
        List<String> errors = new ArrayList<>();
        checkValues(req.getName(), req.getCoords(), errors);
       if (!errors.isEmpty()) {
           return new ResponseEntity<>(new DataError(errors.get(0)), HttpStatus.BAD_REQUEST);
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
        List<String> errors = new ArrayList<>();
        checkValues(req.getName(), req.getCoords(), errors);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new DataError(errors.get(0)), HttpStatus.BAD_REQUEST);
        }

        Scanner scanner = new Scanner(req.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();
        Location location = locationsService.createLocation(req.getName(), lat, lon);
        return new ResponseEntity(location, HttpStatus.OK);
    }
}

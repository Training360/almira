package locationsapp.controller;

import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
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
    public ResponseEntity<Object> getLocationById(@PathVariable long id) {
        var location = locationsService.getLocationById(id);
        if (location.isEmpty()) {
            return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
        }
        else {
            return ResponseEntity.ok(location.get());
        }
    }

    @RequestMapping(value = "/api/locations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteLocation(@PathVariable long id) {
        int i = locationsService.deleteLocation(id);
        if (i == 0) {
            return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
        }
        else {
            return ResponseEntity.ok(new DeleteLocationResponse());
        }
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

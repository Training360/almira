package locationsapp.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.util.*;

@RestController
public class LocationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationsController.class);

    private LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.GET)
    public Page<Location> listLocations(@PageableDefault(sort = "name") Pageable pageable) {
        return locationsService.listLocations(pageable);
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
        var location = locationsService.deleteLocation(id);
        if (location.isEmpty()) {
            return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
        }
        else {
            return ResponseEntity.ok(location.get());
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

        var location = locationsService.updateLocation(id, req);
       if (location.isEmpty()) {
           return new ResponseEntity<>(new DataError("Not found"), HttpStatus.NOT_FOUND);
       }
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/locations", method = RequestMethod.POST)
    public ResponseEntity<Object> createLocation(@RequestBody CreateLocationRequest req) {
        List<String> errors = new ArrayList<>();
        checkValues(req.getName(), req.getCoords(), errors);
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new DataError(errors.get(0)), HttpStatus.BAD_REQUEST);
        }

        Location location = locationsService.createLocation(req);
        return new ResponseEntity(location, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            if (((InvalidFormatException) ex.getCause()).getPath().toString().contains("interestingAt")) {
                return new ResponseEntity<>(new DataError("Invalid Interesting at format!"), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new DataError(ex.getMessage()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new DataError(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

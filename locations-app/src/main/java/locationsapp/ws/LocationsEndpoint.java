package locationsapp.ws;

import locationsapp.controller.Validator;
import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://locations.com/services/locations")
@Service
public class LocationsEndpoint {

    private LocationsService locationsService;

    public LocationsEndpoint(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @WebMethod
    @XmlElementWrapper(name = "locations")
    @WebResult(name = "location")
    public List<LocationDto> listLocations() {
        return locationsService.listLocations().stream()
                .map(LocationDto::new).collect(Collectors.toList());
    }

    @WebResult(name = "location")
    public Location createLocation(@WebParam(name = "createLocationRequest") @XmlElement(required = true) CreateLocationRequest createLocationRequest) {
        List<String> errors = new ArrayList<>();
        new Validator().validate(createLocationRequest.getName(), createLocationRequest.getLat(), createLocationRequest.getLon(), errors);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }
        // TODO
        return null;
    }

    @WebResult(name = "location")
    public Location updateLocation(@WebParam(name = "updateLocationRequest") @XmlElement(required = true) UpdateLocationRequest location) {
        List<String> errors = new ArrayList<>();
        new Validator().validate(location.getName(), location.getLat(), location.getLon(), errors);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }
        // TODO
        return null;
    }

    @WebMethod
    @WebResult(name = "status")
    public String deleteLocation(@WebParam(name = "locationId") long id) {
        var location = locationsService.deleteLocation(id);
        if (location.isEmpty()) {
            throw new IllegalArgumentException("Unknown id: " + id);
        }
        return "deleted";
    }
}

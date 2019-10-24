package locationsapp.ws;

import locationsapp.controller.CreateLocationCommand;
import locationsapp.controller.LocationValidator;
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
                .map(this::toDto).collect(Collectors.toList());
    }

    @WebResult(name = "location")
    public LocationDto createLocation(@WebParam(name = "createLocationRequest") @XmlElement(required = true) CreateLocationRequest createLocationRequest) {
        List<String> errors = new ArrayList<>();
        new LocationValidator().validate(createLocationRequest.getName(), createLocationRequest.getLat(), createLocationRequest.getLon(), errors);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }

        var location = locationsService.createLocation(toCommand(createLocationRequest));
        return toDto(location);
    }

    private CreateLocationCommand toCommand(CreateLocationRequest request) {
        var command = new CreateLocationCommand();
        command.setName(request.getName());
        command.setCoords(request.getLat() + "," + request.getLon());
        command.setInterestingAt(request.getInterestingAt());
        command.setTags(request.getTags());
        return command;
    }

    private CreateLocationCommand toCommand(UpdateLocationRequest request) {
        var command = new CreateLocationCommand();
        command.setName(request.getName());
        command.setCoords(request.getLat() + "," + request.getLon());
        command.setInterestingAt(request.getInterestingAt());
        command.setTags(request.getTags());
        return command;
    }

    private LocationDto toDto(Location location) {
        var dto = new LocationDto();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());
        dto.setInterestingAt(location.getInterestingAt());
        if (!location.getTags().isEmpty()) {
            dto.setTags(String.join(",", location.getTags()));
        }
        return dto;
    }

    @WebResult(name = "location")
    public LocationDto updateLocation(@WebParam(name = "updateLocationRequest") @XmlElement(required = true) UpdateLocationRequest request) {
        List<String> errors = new ArrayList<>();
        new LocationValidator().validate(request.getName(), request.getLat(), request.getLon(), errors);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }

        var location = locationsService.updateLocation(request.getId(), toCommand(request));
        if (location.isEmpty()) {
            throw new IllegalArgumentException("Unknown id: " + request.getId());
        }
        return toDto(location.get());
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

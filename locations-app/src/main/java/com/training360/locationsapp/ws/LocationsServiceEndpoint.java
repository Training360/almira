package com.training360.locationsapp.ws;

import com.training360.locationsapp.entities.Location;
import com.training360.locationsapp.service.LocationsService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.stream.Collectors;

@Endpoint
public class LocationsServiceEndpoint {

    private static final String NAMESPACE_URI = "http://training360.com/locations";


    private LocationsService locationsService;

    public LocationsServiceEndpoint(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listLocationsRequest")
    @ResponsePayload
    public ListLocationsResponse listLocations(@RequestPayload ListLocationsRequest listLocationsRequest) {
        return new ListLocationsResponse(
                locationsService.listLocations().stream().map(x->
                        new LocationDto(x.getId(), x.getName(), x.getLat(), x.getLon())).collect(Collectors.toList()));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createLocationRequest")
    @ResponsePayload
    public CreateLocationResponse createLocation(@RequestPayload CreateLocationRequest req) {
        Location location = locationsService.createLocation(req.getName(), req.getLat(), req.getLon());
        return new CreateLocationResponse(new LocationDto(location.getId(), location.getName(), location.getLat(), location.getLon()));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateLocationRequest")
    @ResponsePayload
    public UpdateLocationResponse updateLocation(@RequestPayload UpdateLocationRequest req) {
        Location location = locationsService.updateLocation(req.getId(), req.getName(), req.getLat(), req.getLon());
        return new UpdateLocationResponse(new LocationDto(location.getId(), location.getName(), location.getLat(), location.getLon()));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteLocationRequest")
    @ResponsePayload
    public DeleteLocationResponse deleteLocation(@RequestPayload DeleteLocationRequest req) {
        int count = locationsService.deleteLocation(req.getId());
        return new DeleteLocationResponse(count == 1);
    }
}

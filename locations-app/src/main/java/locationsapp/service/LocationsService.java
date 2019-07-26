package locationsapp.service;

import locationsapp.controller.ListLocationsResponse;
import locationsapp.entities.Location;
import locationsapp.repository.LocationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationsService.class);

    private LocationsRepository locationsRepository;

    public LocationsService(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public ListLocationsResponse listLocations(int start, int size) {
        List<Location> locations = locationsRepository.listLocations(start, size);
        int sum = locationsRepository.sumOfLocations();
        return new ListLocationsResponse(locations, start, sum);
    }

    public List<Location> listLocations() {
        return locationsRepository.listLocations();
    }

    public Location createLocation(String name, double lat, double lon) {
        Location location = locationsRepository.createLocation(name, lat, lon);
        LOGGER.info(String.format("Location has created id: %s, name: %s", location.getId(), name));
        return location;
    }

    public Location getLocationById(long id) {
        return locationsRepository.getLocationById(id);
    }

    public Location updateLocation(long id, String name, double lat, double lon) {
        Location location = locationsRepository.updateLocation(id, name, lat, lon);
        LOGGER.info(String.format("Location has updated id: %s, name: %s", id, name));
        return location;
    }

    public int deleteLocation(long id) {
       int count = locationsRepository.deleteLocation(id);
       LOGGER.info(String.format("Location has deleted, id: %s", id));
       return count;
    }
}

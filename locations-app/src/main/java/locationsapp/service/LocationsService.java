package locationsapp.service;

import locationsapp.controller.CreateLocationCommand;
import locationsapp.entities.Location;
import locationsapp.repository.LocationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationsService.class);

    private LocationsRepository locationsRepository;

    public LocationsService(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public Page<Location> listLocations(Pageable pageable) {
        return locationsRepository.findAllWithTags(pageable);
    }

    public List<Location> listLocations() {
        return locationsRepository.findAllWithTags(Pageable.unpaged()).getContent();
    }

    public Location createLocation(CreateLocationCommand command) {
        Scanner scanner = new Scanner(command.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();

        Location location = new Location();
        location.setLat(lat);
        location.setLon(lon);
        location.setName(command.getName());
        location.setInterestingAt(command.getInterestingAt());
        location.setTags(parseTags(command.getTags()));

        locationsRepository.save(location);
        LOGGER.info(String.format("Location has created id: %s, name: %s", location.getId(), command.getName()));
        return location;
    }

    public Optional<Location> getLocationById(long id) {
        return locationsRepository.findByIdWithTags(id);
    }

    @Transactional
    public Optional<Location> updateLocation(long id, CreateLocationCommand req) {
        var maybeLocation = locationsRepository.findById(id);
        if (maybeLocation.isEmpty()) {
            return Optional.empty();
        }
        var location = maybeLocation.get();
        Scanner scanner = new Scanner(req.getCoords()).useDelimiter(",")
                .useLocale(Locale.UK);
        double lat = scanner.nextDouble();
        double lon = scanner.nextDouble();

        location.setName(req.getName());
        location.setLat(lat);
        location.setLon(lon);
        location.setInterestingAt(req.getInterestingAt());
        location.setTags(parseTags(req.getTags()));

        LOGGER.info(String.format("Location has updated id: %s, name: %s", id, req.getName()));
        return Optional.of(location);
    }

    public Optional<Location> deleteLocation(long id) {
        var location = locationsRepository.findByIdWithTags(id);
        if (location.isEmpty()) {
            return Optional.empty();
        }
        else {
            locationsRepository.delete(location.get());
            LOGGER.info(String.format("Location has deleted, id: %s", id));
            return Optional.of(location.get());
        }
    }

    private List<String> parseTags(String tags) {
        if (tags == null) {
            return List.of();
        }
        return Arrays.stream(tags.split(",")).map(s -> s.trim()).filter(s -> !s.isBlank()).collect(Collectors.toList());
    }

}

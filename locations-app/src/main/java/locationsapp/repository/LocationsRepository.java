package locationsapp.repository;

import locationsapp.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationsRepository extends JpaRepository<Location, Long> {

    @Query(value = "select distinct l from Location l left join fetch l.tags",
    countQuery = "select count(l) from Location l")
    Page<Location> findAllWithTags(Pageable pageable);

}

package de.slauth.peakwork.nominatim.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResolvedPlaceRepository extends JpaRepository<ResolvedPlace, NominatimId>, JpaSpecificationExecutor<ResolvedPlace> {

    @Query(nativeQuery = true, value = "SELECT * FROM resolved_place WHERE last_updated < CURRENT_TIMESTAMP - INTERVAL '12 SEC' FOR UPDATE SKIP LOCKED LIMIT 1")
    Optional<ResolvedPlace> lockForUpdate();
}

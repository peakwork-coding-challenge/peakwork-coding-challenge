package de.slauth.peakwork.nominatim.updateservice;

import com.fasterxml.jackson.databind.JsonNode;
import de.slauth.peakwork.nominatim.client.NominatimClient;
import de.slauth.peakwork.nominatim.repo.NominatimId;
import de.slauth.peakwork.nominatim.repo.ResolvedPlace;
import de.slauth.peakwork.nominatim.repo.ResolvedPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledUpdate {

    private final NominatimClient nominatimClient;
    private final ResolvedPlaceRepository resolvedPlaceRepository;

    @Transactional
    @Scheduled(fixedDelayString = "PT5M")
    public void update() {
        while (true) {
            Optional<ResolvedPlace> needsUpdate = resolvedPlaceRepository.lockForUpdate();
            if (needsUpdate.isEmpty()) { // everything is up to date
                return;
            }
            ResolvedPlace resolvedPlace = needsUpdate.get();
            NominatimId id = resolvedPlace.getId();
            JsonNode result = nominatimClient.resolve(id.getOsmType(), id.getOsmId());
            resolvedPlace.setJson(result);
            resolvedPlaceRepository.save(resolvedPlace);
            log.info("Successfully updated outdated place: {}", v("id", id));
        }
    }
}

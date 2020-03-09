package de.slauth.peakwork.nominatim.saveservice;

import com.fasterxml.jackson.databind.JsonNode;
import de.slauth.peakwork.nominatim.repo.ResolvedPlaceRepository;
import de.slauth.peakwork.nominatim.repo.ResolvedPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final ResolvedPlaceMapper resolvedPlaceMapper;
    private final ResolvedPlaceRepository resolvedPlaceRepository;

    public void save(JsonNode result) {
        ResolvedPlace resolvedPlace = resolvedPlaceMapper.apply(result);
        resolvedPlaceRepository.save(resolvedPlace);
    }
}

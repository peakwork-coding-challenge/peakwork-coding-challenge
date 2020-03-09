package de.slauth.peakwork.nominatim.saveservice;

import com.fasterxml.jackson.databind.JsonNode;
import de.slauth.peakwork.nominatim.repo.ResolvedPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ResolvedPlaceMapper implements Function<JsonNode, ResolvedPlace> {

    private final NominatimIdMapper nominatimIdMapper;

    @Override
    public ResolvedPlace apply(JsonNode source) {
        ResolvedPlace target = new ResolvedPlace();
        target.setId(nominatimIdMapper.apply(source));
        target.setCountryCode(mapCountryCode(source));
        target.setJson(source);
        return target;
    }

    private String mapCountryCode(JsonNode source) {
        JsonNode address = source.get("address");
        if (address == null || address.isNull()) {
            return null;
        }
        JsonNode node = address.get("country_code");
        if (node == null) {
            return null;
        } else if (!node.isTextual()) {
            throw new IllegalArgumentException("Invalid country_code");
        }
        return node.textValue();
    }
}

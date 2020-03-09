package de.slauth.peakwork.nominatim.saveservice;

import com.fasterxml.jackson.databind.JsonNode;
import de.slauth.peakwork.nominatim.repo.NominatimId;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class NominatimIdMapper implements Function<JsonNode, NominatimId> {

    @Override
    public NominatimId apply(JsonNode source) {
        NominatimId target = new NominatimId();
        target.setOsmType(mapOsmType(source));
        target.setOsmId(mapOsmId(source));
        return target;
    }

    private String mapOsmType(JsonNode source) {
        JsonNode node = source.get("osm_type");
        if (node == null) {
            throw new IllegalArgumentException("Missing osm_type");
        } else if (!node.isTextual()) {
            throw new IllegalArgumentException("Invalid osm_type");
        }
        String value = node.textValue();
        switch (node.textValue()) {
            case "node":
                return "N";
            case "way":
                return "W";
            case "relation":
                return "R";
            default:
                throw new IllegalArgumentException("Unknown osm_type: " + value);
        }
    }

    private long mapOsmId(JsonNode source) {
        JsonNode node = source.get("osm_id");
        if (node == null) {
            throw new IllegalArgumentException("Missing osm_id");
        } else if (!node.canConvertToLong()) {
            throw new IllegalArgumentException("Invalid osm_id");
        }
        return node.asLong();
    }
}

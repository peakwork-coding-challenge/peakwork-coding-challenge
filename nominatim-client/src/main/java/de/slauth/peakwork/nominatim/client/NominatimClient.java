package de.slauth.peakwork.nominatim.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.springframework.http.HttpMethod.GET;

@Component
public class NominatimClient {

    private final HttpHeaders headers;
    private final RestTemplate restTemplate;

    public NominatimClient(RestTemplateBuilder restTemplateBuilder) {
        this.headers = new HttpHeaders();
        this.headers.set(HttpHeaders.USER_AGENT, "peakwork-nominatim-resolver");
        this.restTemplate = restTemplateBuilder
                .rootUri("https://nominatim.openstreetmap.org")
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    public JsonNode resolve(double lat, double lon) {
        return get("/reverse?lat={lat}&lon={lon}&format=jsonv2", lat, lon);
    }

    public JsonNode resolve(String osmType, long osmId) {
        return get("/reverse?osm_type={osm_type}&osm_id={osm_id}&format=jsonv2", osmType, osmId);
    }

    private JsonNode get(String url, Object... uriVariables) {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, GET, requestEntity, JsonNode.class, uriVariables);
        return responseEntity.getBody();
    }
}

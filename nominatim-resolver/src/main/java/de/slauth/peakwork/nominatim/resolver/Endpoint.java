package de.slauth.peakwork.nominatim.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import de.slauth.peakwork.nominatim.client.NominatimClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/resolve")
public class Endpoint {

    private final NominatimClient nominatimClient;
    private final Publisher publisher;

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public JsonNode resolve(@RequestParam double lat, @RequestParam double lon) {
        JsonNode result = nominatimClient.resolve(lat, lon);
        publisher.publish(result);
        return result;
    }
}

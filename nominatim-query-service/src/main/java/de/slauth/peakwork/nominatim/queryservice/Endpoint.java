package de.slauth.peakwork.nominatim.queryservice;

import de.slauth.peakwork.nominatim.repo.ResolvedPlace;
import de.slauth.peakwork.nominatim.repo.ResolvedPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static de.slauth.peakwork.nominatim.queryservice.RestCollection.toRestCollection;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/query")
public class Endpoint {

    private final ResolvedPlaceRepository resolvedPlaceRepository;

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public RestCollection<ResolvedPlace> query(

            @RequestParam(value = "page", defaultValue = "0")
                    int page,

            @RequestParam(value = "size", defaultValue = "10")
                    int size,

            @RequestParam(value = "country_code", required = false)
                    String countryCode
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        ResolvedPlaceSpecification specification = ResolvedPlaceSpecification.builder()
                .countryCode(countryCode)
                .build();
        Page<ResolvedPlace> result = resolvedPlaceRepository.findAll(specification, pageRequest);
        return toRestCollection(result);
    }
}

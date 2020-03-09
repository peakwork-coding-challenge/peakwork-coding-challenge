package de.slauth.peakwork.nominatim.repo;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class NominatimId implements Serializable {

    private String osmType;
    private long osmId;
}

package de.slauth.peakwork.nominatim.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.Instant;

@Data
@Entity
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class ResolvedPlace {

    @EmbeddedId
    private NominatimId id;

    private String countryCode;

    @UpdateTimestamp
    private Instant lastUpdated;

    @Type(type = "jsonb")
    private JsonNode json;
}

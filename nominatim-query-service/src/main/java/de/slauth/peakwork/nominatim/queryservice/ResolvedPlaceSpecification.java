package de.slauth.peakwork.nominatim.queryservice;

import de.slauth.peakwork.nominatim.repo.ResolvedPlace;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Builder
public class ResolvedPlaceSpecification implements Specification<ResolvedPlace> {

    private String countryCode;

    @Override
    public Predicate toPredicate(Root<ResolvedPlace> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (countryCode == null) {
            return builder.and();
        } else {
            return builder.equal(root.get("countryCode"), countryCode);
        }
    }
}

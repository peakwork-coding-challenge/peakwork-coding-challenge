package de.slauth.peakwork.nominatim.queryservice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class RestCollection<T> {

    public static <T> RestCollection<T> toRestCollection(Page<T> page) {
        return RestCollection.<T>builder()
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }

    private final int numberOfElements;
    private final long totalElements;
    private final int totalPages;
    private final List<T> content;
}

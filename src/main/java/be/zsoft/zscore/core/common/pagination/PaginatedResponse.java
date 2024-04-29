package be.zsoft.zscore.core.common.pagination;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public record PaginatedResponse<T>(
        List<T> items,
        int currentPage,
        int count,
        int lastPage,
        long total,
        PaginationLinks links
) {
    public static <T> PaginatedResponse<T> createResponse(Page<T> page, String baseUrl) {
        return createResponse(page, baseUrl, Map.of());
    }

    public static <T> PaginatedResponse<T> createResponse(Page<T> page, String baseUrl, Map<String, String> queryParams) {
        StringBuilder linkPostfix = new StringBuilder("&size=" + page.getNumberOfElements());

        queryParams.forEach((key, value) -> linkPostfix.append("&")
                .append(key)
                .append("=")
                .append(value));

        int currentPage = page.getNumber() + 1;

        String first = baseUrl + "?page=1" + linkPostfix;
        String last = baseUrl + "?page=" + page.getTotalPages() + linkPostfix;
        String self = baseUrl + "?page=" + currentPage + linkPostfix;
        String prev = currentPage > 1 ? baseUrl + "?page=" + (currentPage - 1) + linkPostfix : null;
        String next = currentPage < page.getTotalPages() ? baseUrl + "?page=" + (currentPage + 1) + linkPostfix : null;

        PaginationLinks links = new PaginationLinks(first, last, self, prev, next);

        return new PaginatedResponse<>(page.getContent(), currentPage, page.getNumberOfElements(), page.getTotalPages(), page.getTotalElements(), links);
    }
}

package be.zsoft.zscore.core.common.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;

public record PaginationLinks(
        String first,
        String last,
        String self,
        @JsonInclude String prev,
        @JsonInclude String next
) {
}

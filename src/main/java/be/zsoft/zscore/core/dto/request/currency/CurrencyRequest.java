package be.zsoft.zscore.core.dto.request.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CurrencyRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 5) String key
) {
}

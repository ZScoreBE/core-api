package be.zsoft.zscore.core.dto.response.currency;

import java.util.UUID;

public record CurrencyResponse(
        UUID id,
        String name,
        String key
) {
}

package be.zsoft.zscore.core.dto.response.currency;

import java.math.BigDecimal;
import java.util.UUID;

public record CurrencyOfferResponse(
        UUID id,
        String name,
        String key,
        int amount,
        BigDecimal priceEx,
        BigDecimal discountPriceEx
) {
}

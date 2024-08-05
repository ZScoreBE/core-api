package be.zsoft.zscore.core.dto.request.currency;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CurrencyOfferRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 5) String key,
        @NotNull @Min(1) int amount,
        @NotNull @Min(0) BigDecimal priceEx,
        @Min(0) BigDecimal discountPriceEx
) {
}

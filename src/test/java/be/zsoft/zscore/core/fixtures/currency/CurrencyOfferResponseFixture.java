package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class CurrencyOfferResponseFixture {

    public static CurrencyOfferResponse aDefaultCurrencyOfferRequest() {
        return new CurrencyOfferResponse(
                UUID.randomUUID(),
                "GOLD",
                "GLD",
                1000,
                new BigDecimal("10.00"),
                new BigDecimal("5.50")
        );
    }
}

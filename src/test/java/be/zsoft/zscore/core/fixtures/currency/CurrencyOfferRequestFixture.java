package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class CurrencyOfferRequestFixture {

    public static CurrencyOfferRequest aDefaultCurrencyOfferRequest() {
        return new CurrencyOfferRequest(
                "GOLD",
                "GLD",
                1000,
                new BigDecimal("10.00"),
                new BigDecimal("5.50")
        );
    }
}

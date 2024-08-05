package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CurrencyResponseFixture {

    public static CurrencyResponse aDefaultCurrencyResponse() {
        return new CurrencyResponse(UUID.randomUUID(), "GOLD", "GLD");
    }
}

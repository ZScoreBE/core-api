package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CurrencyRequestFixture {

    public static CurrencyRequest aDefaultCurrencyRequest() {
        return new CurrencyRequest("GOLD","GLD");
    }

}

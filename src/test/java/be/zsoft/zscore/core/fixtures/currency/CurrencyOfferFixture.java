package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class CurrencyOfferFixture {

    public static CurrencyOffer aDefaultCurrencyOffer() {
        return CurrencyOffer.builder()
                .id(UUID.randomUUID())
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();
    }
}

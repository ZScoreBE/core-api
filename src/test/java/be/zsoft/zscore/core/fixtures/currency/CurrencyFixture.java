package be.zsoft.zscore.core.fixtures.currency;

import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CurrencyFixture {

    public static Currency aDefaultCurrency() {
        return Currency.builder()
                .id(UUID.randomUUID())
                .name("GOLD")
                .key("GLD")
                .game(GameFixture.aDefaultGame())
                .build();
    }
}

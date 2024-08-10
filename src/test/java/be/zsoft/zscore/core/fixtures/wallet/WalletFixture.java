package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WalletFixture {

    public static Wallet aDefaultWallet() {
        return Wallet.builder()
                .id(UUID.randomUUID())
                .amount(2000)
                .currency(CurrencyFixture.aDefaultCurrency())
                .player(PlayerFixture.aDefaultPlayer())
                .build();
    }
}

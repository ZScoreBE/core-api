package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.fixtures.currency.CurrencyResponseFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WalletResponseFixture {

    public static WalletResponse aDefaultWalletResponse() {
        return new WalletResponse(
                UUID.randomUUID(),
                2000,
                CurrencyResponseFixture.aDefaultCurrencyResponse()
        );
    }
}

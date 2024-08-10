package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WalletRequestFixture {

    public static WalletRequest aDefaultWalletRequest() {
        return new WalletRequest(2000, UUID.randomUUID());
    }
}

package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WalletOperationFixture {

    public static WalletOperation aDefaultWalletOperation() {
        return WalletOperation.builder()
                .id(UUID.randomUUID())
                .type(WalletOperationType.INCREASE)
                .amount(2000)
                .wallet(WalletFixture.aDefaultWallet())
                .build();
    }
}

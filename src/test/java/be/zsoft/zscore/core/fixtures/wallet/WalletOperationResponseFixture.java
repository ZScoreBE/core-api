package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.dto.response.wallet.WalletOperationResponse;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class WalletOperationResponseFixture {

    public static WalletOperationResponse aDefaultWalletOperationResponse() {
        return new WalletOperationResponse(
                UUID.randomUUID(),
                WalletOperationType.INCREASE,
                2000,
                WalletResponseFixture.aDefaultWalletResponse()
        );
    }
}

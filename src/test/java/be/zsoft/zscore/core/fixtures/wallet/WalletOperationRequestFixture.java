package be.zsoft.zscore.core.fixtures.wallet;

import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WalletOperationRequestFixture {

    public static WalletOperationRequest aDefaultWalletOperationRequest() {
        return new WalletOperationRequest(WalletOperationType.INCREASE, 2000);
    }

    public static WalletOperationRequest aDecreaseWalletOperation() {
        return new WalletOperationRequest(WalletOperationType.DECREASE, 2000);
    }
}

package be.zsoft.zscore.core.dto.request.wallet;

import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import jakarta.validation.constraints.NotNull;

public record WalletOperationRequest(
        @NotNull WalletOperationType type,
        @NotNull Integer amount
        ) {
}

package be.zsoft.zscore.core.dto.response.wallet;

import be.zsoft.zscore.core.entity.wallet.WalletOperationType;

import java.util.UUID;

public record WalletOperationResponse(
        UUID id,
        WalletOperationType type,
        int amount,
        WalletResponse wallet
) {
}

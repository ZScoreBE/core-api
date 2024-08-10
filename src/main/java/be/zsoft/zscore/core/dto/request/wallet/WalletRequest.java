package be.zsoft.zscore.core.dto.request.wallet;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record WalletRequest(
        @NotNull Integer initialAmount,
        @NotNull UUID currencyId
) {
}

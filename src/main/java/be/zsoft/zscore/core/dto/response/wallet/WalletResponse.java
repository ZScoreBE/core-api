package be.zsoft.zscore.core.dto.response.wallet;

import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;

import java.util.UUID;

public record WalletResponse(
        UUID id,
        int amount,
        CurrencyResponse currency
) {
}

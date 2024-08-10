package be.zsoft.zscore.core.dto.mapper.wallet;

import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WalletMapper {

    private final CurrencyMapper currencyMapper;

    public Wallet fromRequest(WalletRequest request) {
        return Wallet.builder()
                .amount(request.initialAmount())
                .build();
    }

    public WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getAmount(),
                currencyMapper.toResponse(wallet.getCurrency())
        );
    }

    public Page<WalletResponse> toResponse(Page<Wallet> wallets) {
        return wallets.map(this::toResponse);
    }
}

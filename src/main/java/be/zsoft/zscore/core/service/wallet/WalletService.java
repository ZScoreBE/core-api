package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.repository.wallet.WalletRepo;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepo walletRepo;
    private final WalletMapper walletMapper;
    private final CurrencyService currencyService;
    private final GameService gameService;

    public Wallet createWallet(WalletRequest request, Player player) {
        Currency currency = currencyService.getCurrencyById(gameService.getAuthenicatedGame() ,request.currencyId());

        if (walletRepo.existsByPlayerAndCurrency(player, currency)) {
            throw new ApiException(ErrorCodes.WALLET_ALREADY_EXISTS);
        }

        Wallet wallet = walletMapper.fromRequest(request);
        wallet.setCurrency(currency);
        wallet.setPlayer(player);

        return walletRepo.saveAndFlush(wallet);
    }

    public Page<Wallet> getWalletsByPlayer(Player player, Pageable pageable) {
        return walletRepo.findAllByPlayer(player, pageable);
    }

    public Wallet getWalletById(UUID id, Player player) {
        return walletRepo.findByIdAndPlayer(id, player)
                .orElseThrow(() -> new NotFoundException("Could not find wallet with id '%s' and player '%s'".formatted(id, player.getId())));
    }
}

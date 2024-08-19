package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import be.zsoft.zscore.core.repository.wallet.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepo walletRepo;
    private final WalletOperationService walletOperationService;

    public void createWallets(Currency currency, List<Player> players) {
        List<Wallet> wallets = players.stream()
                .map(player -> new Wallet(null, 0, player, currency))
                .toList();

        walletRepo.saveAllAndFlush(wallets);
    }

    public void createWallets(List<Currency> currencies, Player player) {
        List<Wallet> wallets = currencies.stream()
                .map(currency -> new Wallet(null, 0, player, currency))
                .toList();

        walletRepo.saveAllAndFlush(wallets);
    }

    public Page<Wallet> getWalletsByPlayer(Player player, Pageable pageable) {
        return walletRepo.findAllByPlayer(player, pageable);
    }

    public Wallet getWalletById(UUID id, Player player) {
        return walletRepo.findByIdAndPlayer(id, player)
                .orElseThrow(() -> new NotFoundException("Could not find wallet with id '%s' and player '%s'".formatted(id, player.getId())));
    }

    public Wallet updateWalletAmount(UUID id, Player player, WalletOperationRequest request) {
        Wallet wallet = getWalletById(id, player);

        return updateWalletAmount(wallet, request.type(), request.amount());
    }

    public void takeCurrency(Player player, Currency currency, Integer amount) {
        Wallet wallet = walletRepo.findByCurrencyAndPlayer(currency, player)
                .orElseThrow(() -> new NotFoundException("Could not find any wallet with currency '%s' and player '%s'".formatted(currency.getId(), player.getId())));

        updateWalletAmount(wallet, WalletOperationType.DECREASE,  amount);
    }

    public void giveCurrency(Player player, Currency currency, Integer amount) {
        Wallet wallet = walletRepo.findByCurrencyAndPlayer(currency, player)
                .orElseThrow(() -> new NotFoundException("Could not find any wallet with currency '%s' and player '%s'".formatted(currency.getId(), player.getId())));

        updateWalletAmount(wallet, WalletOperationType.INCREASE,  amount);
    }

    private Wallet updateWalletAmount(Wallet wallet, WalletOperationType operationType, int amount) {
        if (operationType == WalletOperationType.DECREASE) {
            if (amount > wallet.getAmount()) {
                throw new ApiException(ErrorCodes.WALLET_DOES_NOT_HAVE_ENOUGH_AMOUNT);
            }

            wallet.setAmount(wallet.getAmount() - amount);
        } else {
            wallet.setAmount(wallet.getAmount() + amount);
        }

        walletOperationService.createWalletOperation(new WalletOperationRequest(operationType, amount), wallet);

        return walletRepo.saveAndFlush(wallet);
    }
}

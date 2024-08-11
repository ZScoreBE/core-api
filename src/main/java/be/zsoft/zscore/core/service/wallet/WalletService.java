package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
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
}

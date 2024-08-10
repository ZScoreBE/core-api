package be.zsoft.zscore.core.repository.wallet;

import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, UUID> {

    @Query("SELECT w FROM Wallet w WHERE w.player = :player")
    Page<Wallet> findAllByPlayer(Player player, Pageable pageable);

    @Query("SELECT w FROM Wallet w WHERE w.id = :id AND w.player = :player")
    Optional<Wallet> findByIdAndPlayer(UUID id, Player player);

    @Query("SELECT count(w) > 0 FROM Wallet w WHERE w.player = :player AND w.currency = :currency")
    boolean existsByPlayerAndCurrency(Player player, Currency currency);
}

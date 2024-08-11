package be.zsoft.zscore.core.repository.currency;

import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, UUID> {

    @Query("SELECT c FROM Currency c WHERE c.game = :game ORDER BY c.name")
    Page<Currency> findAllByGame(Game game, Pageable pageable);

    @Query("SELECT c FROM Currency c WHERE c.game = :game ORDER BY c.name")
    List<Currency> findAllByGame(Game game);

    @Query("SELECT c FROM Currency c WHERE c.game = :game AND (LOWER(c.name) LIKE :search OR LOWER(c.key) LIKE :search)")
    Page<Currency> searchAllOnNameOrKeyByGame(String search, Game game, Pageable pageable);

    @Query("SELECT c FROM Currency c WHERE c.id = :id AND c.game = :game")
    Optional<Currency> findByIdAndGame(UUID id, Game game);
}

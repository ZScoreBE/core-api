package be.zsoft.zscore.core.repository.player;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepo extends JpaRepository<Player, UUID> {

    @Query("SELECT p FROM Player p WHERE p.game = :game ORDER BY p.name ASC")
    Page<Player> findAllByGame(Game game, Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.game = :game AND p.name LIKE :search ORDER BY p.name ASC")
    Page<Player> searchAllOnNameByGame(String search, Game game, Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.id = :id AND p.game = :game")
    Optional<Player> findByIdAndGame(UUID id, Game game);

    @Query("SELECT COUNT(p) FROM Player p WHERE p.game = :game")
    long countByGame(Game game);
}

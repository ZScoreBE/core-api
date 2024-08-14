package be.zsoft.zscore.core.repository.player;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerLifeSettingsRepo extends JpaRepository<PlayerLifeSettings, UUID> {

    @Query("SELECT pls FROM PlayerLifeSettings pls WHERE pls.game = :game")
    Optional<PlayerLifeSettings> findByGame(Game game);
}

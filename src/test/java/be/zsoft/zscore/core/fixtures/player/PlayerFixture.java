package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class PlayerFixture {

    public static Player aDefaultPlayer() {
        return Player.builder()
                .id(UUID.randomUUID())
                .name("Player")
                .game(GameFixture.aDefaultGame())
                .currentLives(10)
                .lastLifeUpdate(LocalDateTime.now())
                .build();
    }

    public static Player aPlayerWithCurrentLivesAndLastLifeUpdateNull() {
        return Player.builder()
                .id(UUID.randomUUID())
                .name("Player")
                .game(GameFixture.aDefaultGame())
                .currentLives(null)
                .lastLifeUpdate(null)
                .build();
    }
}

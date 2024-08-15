package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class PlayerLifeSettingsFixture {

    public static PlayerLifeSettings aDefaultPlayerLifeSettings() {
        return PlayerLifeSettings.builder()
                .id(UUID.randomUUID())
                .enabled(true)
                .maxLives(10)
                .giveLifeAfterSeconds(900)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static PlayerLifeSettings aDisabledPlayerLifeSettings() {
        return PlayerLifeSettings.builder()
                .id(UUID.randomUUID())
                .enabled(false)
                .maxLives(10)
                .giveLifeAfterSeconds(900)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static PlayerLifeSettings aPlayerLifeSettingsWithNoAutomaticLives() {
        return PlayerLifeSettings.builder()
                .id(UUID.randomUUID())
                .enabled(true)
                .maxLives(10)
                .giveLifeAfterSeconds(null)
                .game(GameFixture.aDefaultGame())
                .build();
    }
}

package be.zsoft.zscore.core.fixtures.game;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.game.GameEngine;
import be.zsoft.zscore.core.fixtures.organization.OrganizationFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GameFixture {

    public static Game aDefaultGame() {
        return Game.builder()
                .id(UUID.randomUUID())
                .build();
    }

    public static Game aDefaultGameWithId(UUID id) {
        return Game.builder()
                .id(id)
                .build();
    }

    public static Game aSandboxGame() {
        return Game.builder()
                .id(UUID.randomUUID())
                .name("sandbox_game")
                .engine(GameEngine.UNITY)
                .active(true)
                .sandboxMode(true)
                .organization(OrganizationFixture.aDefaultOrganization())
                .build();
    }

    public static Game aLiveGame() {
        return Game.builder()
                .id(UUID.randomUUID())
                .name("live_game")
                .engine(GameEngine.UNITY)
                .active(true)
                .sandboxMode(false)
                .organization(OrganizationFixture.aDefaultOrganization())
                .build();
    }
}

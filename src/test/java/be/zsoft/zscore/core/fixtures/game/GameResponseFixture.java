package be.zsoft.zscore.core.fixtures.game;

import be.zsoft.zscore.core.dto.response.game.GameResponse;
import be.zsoft.zscore.core.entity.game.GameEngine;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GameResponseFixture {

    public static GameResponse aDefaultGameResponse() {
        return new GameResponse(
                UUID.randomUUID(),
                "game",
                GameEngine.UNITY,
                true,
                true,
                "key",
                UUID.randomUUID()
        );
    }
}

package be.zsoft.zscore.core.fixtures.game;

import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.entity.game.GameEngine;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameRequestFixture {

    public static GameRequest aDefaultGameRequest() {
        return new GameRequest("game", GameEngine.UNITY);
    }
}

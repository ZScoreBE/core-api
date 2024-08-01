package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerRequestFixtures {

    public static PlayerRequest aDefaultPlayerRequest() {
        return new PlayerRequest("Wout");
    }
}

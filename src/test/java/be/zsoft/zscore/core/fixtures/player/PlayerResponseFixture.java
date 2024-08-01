package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class PlayerResponseFixture {

    public static PlayerResponse aDefaultPlayerResponse() {
        return new PlayerResponse(
                UUID.randomUUID(),
                "wout",
                LocalDateTime.now()
        );
    }
}

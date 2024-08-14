package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.dto.response.player.PlayerLifeSettingsResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class PlayerLifeSettingsResponseFixture {

    public static PlayerLifeSettingsResponse aDefaultPlayerLifeSettingsResponse() {
        return new PlayerLifeSettingsResponse(
                UUID.randomUUID(),
                true,
                10,
                900
        );
    }
}

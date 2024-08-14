package be.zsoft.zscore.core.fixtures.player;

import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerLifeSettingsRequestFixture {

    public static PlayerLifeSettingsRequest aDefaultPlayerLifeSettingsRequest() {
        return new PlayerLifeSettingsRequest(
                true,
                10,
                900
        );
    }
}

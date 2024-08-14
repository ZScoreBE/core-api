package be.zsoft.zscore.core.dto.mapper.player;

import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerLifeSettingsResponse;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import org.springframework.stereotype.Component;

@Component
public class PlayerLifeSettingsMapper {

    public PlayerLifeSettings fromRequest(PlayerLifeSettingsRequest request, PlayerLifeSettings settings) {
        settings.setEnabled(request.enabled());
        settings.setMaxLives(request.maxLives());
        settings.setGiveLifeAfterSeconds(request.giveLifeAfterSeconds());

        return settings;
    }

    public PlayerLifeSettingsResponse toResponse(PlayerLifeSettings settings) {
        return new PlayerLifeSettingsResponse(
                settings.getId(),
                settings.isEnabled(),
                settings.getMaxLives(),
                settings.getGiveLifeAfterSeconds()
        );
    }
}

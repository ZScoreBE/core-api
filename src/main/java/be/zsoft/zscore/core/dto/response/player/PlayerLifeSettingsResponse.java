package be.zsoft.zscore.core.dto.response.player;

import java.util.UUID;

public record PlayerLifeSettingsResponse(
        UUID id,
        boolean enabled,
        int maxLives,
        Integer giveLifeAfterSeconds
) {
}

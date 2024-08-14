package be.zsoft.zscore.core.dto.request.player;

public record PlayerLifeSettingsRequest(
        boolean enabled,
        int maxLives,
        Integer giveLifeAfterSeconds
) {
}

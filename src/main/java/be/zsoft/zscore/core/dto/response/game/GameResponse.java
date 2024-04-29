package be.zsoft.zscore.core.dto.response.game;

import be.zsoft.zscore.core.entity.game.GameEngine;

import java.util.UUID;

public record GameResponse(
        UUID id,
        String name,
        GameEngine engine,
        boolean active,
        boolean sandboxMode,
        String apiKey,
        UUID generationId
) {
}

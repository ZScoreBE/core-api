package be.zsoft.zscore.core.dto.request.game;

import be.zsoft.zscore.core.entity.game.GameEngine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GameRequest(
        @NotBlank String name,
        @NotNull GameEngine engine
) {
}

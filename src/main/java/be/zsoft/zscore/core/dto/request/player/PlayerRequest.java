package be.zsoft.zscore.core.dto.request.player;

import jakarta.validation.constraints.NotBlank;

public record PlayerRequest(
        @NotBlank String name
) {
}

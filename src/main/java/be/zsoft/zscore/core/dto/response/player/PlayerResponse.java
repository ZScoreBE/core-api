package be.zsoft.zscore.core.dto.response.player;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerResponse(
        UUID id,
        String name,
        LocalDateTime lastSignIn,
        Integer lives,
        LocalDateTime lastLifeUpdate
) {
}

package be.zsoft.zscore.core.dto.request.leaderboard;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LeaderboardScoreRequest(
        @NotNull Integer score,
        @NotNull UUID playerId
) {
}

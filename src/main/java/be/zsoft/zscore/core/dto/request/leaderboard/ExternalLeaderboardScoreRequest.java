package be.zsoft.zscore.core.dto.request.leaderboard;

import jakarta.validation.constraints.NotNull;

public record ExternalLeaderboardScoreRequest(
        @NotNull Integer score
) {
}

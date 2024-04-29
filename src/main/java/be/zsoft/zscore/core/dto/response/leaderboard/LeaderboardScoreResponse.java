package be.zsoft.zscore.core.dto.response.leaderboard;

import be.zsoft.zscore.core.dto.response.player.PlayerResponse;

import java.util.UUID;

public record LeaderboardScoreResponse(
        UUID id,
        int score,
        PlayerResponse player
) {
}

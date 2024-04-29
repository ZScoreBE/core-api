package be.zsoft.zscore.core.dto.response.leaderboard;

import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public record LeaderboardResponse(
        UUID id,
        String name,
        Sort.Direction direction,
        LeaderboardScoreType scoreType
) {
}

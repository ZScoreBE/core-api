package be.zsoft.zscore.core.dto.request.leaderboard;

import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record LeaderboardRequest(
        @NotBlank String name,
        @NotNull Sort.Direction direction,
        @NotNull LeaderboardScoreType scoreType
) {
}

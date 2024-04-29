package be.zsoft.zscore.core.dto.mapper.leaderboard;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LeaderboardScoreMapper {

    private final PlayerMapper playerMapper;

    public LeaderboardScoreResponse toResponse(LeaderboardScore leaderboardScore) {
        return new LeaderboardScoreResponse(
                leaderboardScore.getId(),
                leaderboardScore.getScore(),
                playerMapper.toResponse(leaderboardScore.getPlayer())
        );
    }

    public Page<LeaderboardScoreResponse> toResponse(Page<LeaderboardScore> scores) {
        return scores.map(this::toResponse);
    }
}

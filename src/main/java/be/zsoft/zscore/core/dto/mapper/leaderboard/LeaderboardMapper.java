package be.zsoft.zscore.core.dto.mapper.leaderboard;

import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardMapper {

    public Leaderboard fromRequest(LeaderboardRequest request) {
        return fromRequest(request, new Leaderboard());
    }

    public Leaderboard fromRequest(LeaderboardRequest request, Leaderboard leaderboard) {
        leaderboard.setName(request.name());
        leaderboard.setDirection(request.direction());
        leaderboard.setScoreType(request.scoreType());
        return leaderboard;
    }

    public LeaderboardResponse toResponse(Leaderboard leaderboard) {
        return new LeaderboardResponse(
                leaderboard.getId(),
                leaderboard.getName(),
                leaderboard.getDirection(),
                leaderboard.getScoreType()
        );
    }

    public Page<LeaderboardResponse> toResponse(Page<Leaderboard> leaderboards) {
        return leaderboards.map(this::toResponse);
    }
}

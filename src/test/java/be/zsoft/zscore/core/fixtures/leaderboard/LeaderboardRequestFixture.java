package be.zsoft.zscore.core.fixtures.leaderboard;

import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class LeaderboardRequestFixture {

    public static LeaderboardRequest aDefaultLeaderboardRequest() {
        return new LeaderboardRequest(
                "leaderboard",
                Sort.Direction.ASC,
                LeaderboardScoreType.HIGHEST
        );
    }
}

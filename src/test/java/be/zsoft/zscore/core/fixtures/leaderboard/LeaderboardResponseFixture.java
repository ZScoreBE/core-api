package be.zsoft.zscore.core.fixtures.leaderboard;

import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@UtilityClass
public class LeaderboardResponseFixture {

    public static LeaderboardResponse aDefaultLeaderboardResponse() {
        return new LeaderboardResponse(
                UUID.randomUUID(),
                "leaderboard",
                Sort.Direction.ASC,
                LeaderboardScoreType.HIGHEST
        );
    }
}

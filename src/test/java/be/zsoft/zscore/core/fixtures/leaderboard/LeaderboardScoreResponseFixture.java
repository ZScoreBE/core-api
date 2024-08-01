package be.zsoft.zscore.core.fixtures.leaderboard;

import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.fixtures.player.PlayerResponseFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class LeaderboardScoreResponseFixture {

    public static LeaderboardScoreResponse aDefaultLeaderboardScoreResponse() {
        return new LeaderboardScoreResponse(
                UUID.randomUUID(),
                100,
                PlayerResponseFixture.aDefaultPlayerResponse()
        );
    }
}

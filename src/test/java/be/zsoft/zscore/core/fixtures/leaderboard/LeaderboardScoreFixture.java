package be.zsoft.zscore.core.fixtures.leaderboard;

import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class LeaderboardScoreFixture {

    public static LeaderboardScore aDefaultLeaderboardScore() {
        return LeaderboardScore.builder()
                .id(UUID.randomUUID())
                .score(50)
                .player(PlayerFixture.aDefaultPlayer())
                .leaderboard(LeaderboardFixture.aDefaultLeaderboard())
                .build();
    }

    public static LeaderboardScore withScoreLeaderboard(int score) {
        return LeaderboardScore.builder()
                .id(UUID.randomUUID())
                .score(score)
                .player(PlayerFixture.aDefaultPlayer())
                .leaderboard(LeaderboardFixture.aDefaultLeaderboard())
                .build();
    }
}

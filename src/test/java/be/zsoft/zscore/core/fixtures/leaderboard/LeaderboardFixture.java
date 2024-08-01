package be.zsoft.zscore.core.fixtures.leaderboard;

import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@UtilityClass
public class LeaderboardFixture {

    public static Leaderboard aDefaultLeaderboard() {
        return Leaderboard.builder()
                .id(UUID.randomUUID())
                .name("Leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static Leaderboard aHighestScoreTypeLeaderboard() {
        return Leaderboard.builder()
                .id(UUID.randomUUID())
                .name("Highest Score Leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static Leaderboard aLatestScoreTypeLeaderboard() {
        return Leaderboard.builder()
                .id(UUID.randomUUID())
                .name("Latest Score Leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.LATEST)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static Leaderboard aMultipleScoreTypeLeaderboard() {
        return Leaderboard.builder()
                .id(UUID.randomUUID())
                .name("Latest Score Leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.MULTIPLE)
                .game(GameFixture.aDefaultGame())
                .build();
    }
}

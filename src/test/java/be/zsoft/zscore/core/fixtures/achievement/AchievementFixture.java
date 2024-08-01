package be.zsoft.zscore.core.fixtures.achievement;

import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AchievementFixture {

    public static Achievement aDefaultAchievement() {
        return Achievement.builder()
                .id(UUID.randomUUID())
                .name("Achievement 1")
                .description("Achievement description")
                .type(AchievementType.MULTIPLE)
                .neededCount(20)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static Achievement aMultipleAchievement() {
        return Achievement.builder()
                .id(UUID.randomUUID())
                .name("Achievement with multiple type")
                .description("Achievement description")
                .type(AchievementType.MULTIPLE)
                .neededCount(20)
                .game(GameFixture.aDefaultGame())
                .build();
    }

    public static Achievement aSingleAchievement() {
        return Achievement.builder()
                .id(UUID.randomUUID())
                .name("Achievement with single type")
                .description("Achievement description")
                .type(AchievementType.SINGLE)
                .neededCount(20)
                .game(GameFixture.aDefaultGame())
                .build();
    }
}

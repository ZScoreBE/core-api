package be.zsoft.zscore.core.fixtures.achievement;

import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AchievementProgressFixture {

    public static AchievementProgress aDefaultAchievementProgress() {
        return AchievementProgress.builder()
                .id(UUID.randomUUID())
                .completed(false)
                .currentCount(10)
                .achievement(AchievementFixture.aDefaultAchievement())
                .player(PlayerFixture.aDefaultPlayer())
                .achievement(AchievementFixture.aDefaultAchievement())
                .build();
    }

    public static AchievementProgress aCompletedAchievementProgress() {
        return AchievementProgress.builder()
                .id(UUID.randomUUID())
                .completed(true)
                .currentCount(20)
                .achievement(AchievementFixture.aDefaultAchievement())
                .player(PlayerFixture.aDefaultPlayer())
                .achievement(AchievementFixture.aDefaultAchievement())
                .build();
    }
}

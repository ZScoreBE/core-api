package be.zsoft.zscore.core.fixtures.achievement;

import be.zsoft.zscore.core.dto.response.achievement.AchievementProgressResponse;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AchievementProgressResponseFixture {

    public static AchievementProgressResponse aDefaultAchievementProgressResponse() {
        return new AchievementProgressResponse(
                UUID.randomUUID(),
                "Achievement 1",
                "Description 1",
                AchievementType.SINGLE,
                null,
                null,
                false
        );
    }
}

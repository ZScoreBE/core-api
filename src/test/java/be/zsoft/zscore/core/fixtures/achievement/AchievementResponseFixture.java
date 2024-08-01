package be.zsoft.zscore.core.fixtures.achievement;

import be.zsoft.zscore.core.dto.response.achievement.AchievementResponse;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AchievementResponseFixture {

    public static AchievementResponse aDefaultAchievementResponse() {
        return new AchievementResponse(
                UUID.randomUUID(),
                "Achievement",
                "Description",
                AchievementType.MULTIPLE,
                20
        );
    }
}

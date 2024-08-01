package be.zsoft.zscore.core.fixtures.achievement;

import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AchievementRequestFixture {

    public static AchievementRequest aDefaultAchievementRequest() {
        return new AchievementRequest(
                "Achievement",
                "Description",
                AchievementType.MULTIPLE,
                20
        );
    }

    public static UpdateAchievementRequest aDefaultUpdateAchievementRequest() {
        return new UpdateAchievementRequest(
                "Achievement",
                "Description",
                20
        );
    }
}

package be.zsoft.zscore.core.dto.mapper.achievement;

import be.zsoft.zscore.core.dto.response.achievement.AchievementProgressResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AchievementProgressMapper {

    public AchievementProgressResponse toResponse(AchievementProgress achievementProgress) {
        Achievement achievement = achievementProgress.getAchievement();

        return new AchievementProgressResponse(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getType(),
                achievement.getNeededCount(),
                achievementProgress.getCurrentCount(),
                achievementProgress.isCompleted()
        );
    }

    public Page<AchievementProgressResponse> toResponse(Page<AchievementProgress> achievementProgresses) {
        return achievementProgresses.map(this::toResponse);
    }
}

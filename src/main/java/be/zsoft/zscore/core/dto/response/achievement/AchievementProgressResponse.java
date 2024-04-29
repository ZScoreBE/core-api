package be.zsoft.zscore.core.dto.response.achievement;

import be.zsoft.zscore.core.entity.achievement.AchievementType;

import java.util.UUID;

public record AchievementProgressResponse(
        UUID id,
        String name,
        String description,
        AchievementType type,
        Integer neededCount,
        Integer currentCount,
        boolean completed) {
}

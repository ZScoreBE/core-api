package be.zsoft.zscore.core.dto.request.achievement;

import be.zsoft.zscore.core.entity.achievement.AchievementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AchievementRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull AchievementType type,
        @Min(1) Integer neededCount) {
}

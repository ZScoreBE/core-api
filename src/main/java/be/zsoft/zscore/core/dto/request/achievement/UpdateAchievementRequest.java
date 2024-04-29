package be.zsoft.zscore.core.dto.request.achievement;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateAchievementRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Min(1) Integer neededCount) {
}

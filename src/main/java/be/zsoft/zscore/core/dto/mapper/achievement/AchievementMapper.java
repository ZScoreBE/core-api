package be.zsoft.zscore.core.dto.mapper.achievement;

import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.dto.response.achievement.AchievementResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

    public Achievement fromRequest(AchievementRequest request) {
        return Achievement.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type())
                .neededCount(request.neededCount())
                .build();
    }

    public Achievement fromRequest(UpdateAchievementRequest request, Achievement achievement) {
        achievement.setName(request.name());
        achievement.setDescription(request.description());
        achievement.setNeededCount(request.neededCount());
        return achievement;
    }

    public AchievementResponse toResponse(Achievement achievement) {
        return new AchievementResponse(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getType(),
                achievement.getNeededCount()
        );
    }

    public Page<AchievementResponse> toResponse(Page<Achievement> achievements) {
        return achievements.map(this::toResponse);
    }
}

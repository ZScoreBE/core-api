package be.zsoft.zscore.core.controller.external.achievement;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementProgressMapper;
import be.zsoft.zscore.core.dto.response.achievement.AchievementProgressResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.achievement.AchievementService;
import be.zsoft.zscore.core.service.game.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/achievements")
public class AchievementProgressController {

    private final AchievementProgressService achievementProgressService;
    private final GameService gameService;
    private final AchievementService achievementService;
    private final AchievementProgressMapper achievementProgressMapper;

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<AchievementProgressResponse> getAchievements(Pageable pageable) {
        Page<AchievementProgress> progresses = achievementProgressService.getAchievementProgresses(pageable);

        return PaginatedResponse.createResponse(
                achievementProgressMapper.toResponse(progresses),
                "/external/achievements"
        );
    }

    @Secured({"ROLE_PLAYER"})
    @PatchMapping("/{id}/complete")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public AchievementProgressResponse completeAchievement(@PathVariable UUID id) {
        Game game = gameService.getAuthenicatedGame();
        Achievement achievement = achievementService.getAchievementById(game, id);
        AchievementProgress progress = achievementProgressService.completeAchievement(achievement);

        return achievementProgressMapper.toResponse(progress);
    }

    @Secured({"ROLE_PLAYER"})
    @PatchMapping("/{id}/increase")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public AchievementProgressResponse increaseAchievementCount(
            @PathVariable UUID id,
            @RequestParam(name = "amount", defaultValue = "1", required = false) int amount
    ) {
        Game game = gameService.getAuthenicatedGame();
        Achievement achievement = achievementService.getAchievementById(game, id);
        AchievementProgress progress = achievementProgressService.increaseAchievementCount(amount, achievement);

        return achievementProgressMapper.toResponse(progress);
    }

    @Secured({"ROLE_PLAYER"})
    @PatchMapping("/{id}/decrease")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public AchievementProgressResponse decreaseAchievementCount(
            @PathVariable UUID id,
            @RequestParam(name = "amount", defaultValue = "1", required = false) int amount
    ) {
        Game game = gameService.getAuthenicatedGame();
        Achievement achievement = achievementService.getAchievementById(game, id);
        AchievementProgress progress = achievementProgressService.decreaseAchievementCount(amount, achievement);

        return achievementProgressMapper.toResponse(progress);
    }
}

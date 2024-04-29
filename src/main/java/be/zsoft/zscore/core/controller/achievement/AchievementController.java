package be.zsoft.zscore.core.controller.achievement;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementMapper;
import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.dto.response.achievement.AchievementResponse;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.achievement.AchievementService;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games/{gameId}/achievements")
public class AchievementController {

    private final GameService gameService;
    private final AchievementService achievementService;
    private final AchievementMapper achievementMapper;

    @PostMapping
    @Secured({"ROLE_USER"})
    public AchievementResponse createAchievement(
            @PathVariable UUID gameId,
            @Valid  @RequestBody AchievementRequest request
    ) {
        Game game = gameService.getById(gameId);
        Achievement achievement = achievementService.createAchievement(game, request);

        return achievementMapper.toResponse(achievement);
    }

    @GetMapping
    @Secured({"ROLE_USER"})
    public PaginatedResponse<AchievementResponse> getAchievements(
            @PathVariable UUID gameId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Game game = gameService.getById(gameId);
        Page<Achievement> achievements = StringUtils.hasText(search) ?
                achievementService.searchAchievementsByGame(search, game, pageable) :
        achievementService.getAchievementsByGame(game, pageable);

        return PaginatedResponse.createResponse(
                achievementMapper.toResponse(achievements),
                "/games/%s/achievements".formatted(gameId)
        );
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/count")
    public CountResponse countAchievements(@PathVariable("gameId") UUID gameId) {
        Game game = gameService.getById(gameId);
        long achievementCount = achievementService.countAchievementsByGame(game);

        return new CountResponse(achievementCount);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER"})
    public AchievementResponse getAchievement(
            @PathVariable UUID gameId,
            @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        Achievement achievement = achievementService.getAchievementById(game, id);

        return achievementMapper.toResponse(achievement);
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_USER"})
    public AchievementResponse updateAchievement(
            @PathVariable UUID gameId,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAchievementRequest request
    ) {
        Game game = gameService.getById(gameId);
        Achievement achievement = achievementService.updateAchievementById(game, id, request);

        return achievementMapper.toResponse(achievement);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_USER"})
    public void deleteAchievement(
            @PathVariable UUID gameId,
            @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        achievementService.deleteAchievementById(game, id);
    }
}

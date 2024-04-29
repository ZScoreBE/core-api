package be.zsoft.zscore.core.controller.external.achievement;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementProgressMapper;
import be.zsoft.zscore.core.dto.response.achievement.AchievementProgressResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.achievement.AchievementService;
import be.zsoft.zscore.core.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementProgressControllerTest {

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private GameService gameService;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementProgressMapper achievementProgressMapper;

    @InjectMocks
    private AchievementProgressController achievementProgressController;

    @Test
    void getAchievements() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<AchievementProgress> progresses = new PageImpl<>(List.of(
           AchievementProgress.builder().id(UUID.randomUUID()).build(),
           AchievementProgress.builder().id(UUID.randomUUID()).build()
        ));
        Page<AchievementProgressResponse> expected = new PageImpl<>(List.of(
           new AchievementProgressResponse(UUID.randomUUID(), "Achievement 1", "Description 1", AchievementType.SINGLE, null, null, false),
           new AchievementProgressResponse(UUID.randomUUID(), "Achievement 2", "Description 2", AchievementType.SINGLE, null, null, false)
        ));

        when(achievementProgressService.getAchievementProgresses(pageable)).thenReturn(progresses);
        when(achievementProgressMapper.toResponse(progresses)).thenReturn(expected);

        PaginatedResponse<AchievementProgressResponse> result = achievementProgressController.getAchievements(pageable);

        assertEquals(expected.getContent(), result.items());
    }

    @Test
    void completeAchievement() {
        UUID id = UUID.randomUUID();
        Game game = Game.builder().id(UUID.randomUUID()).build();
        Achievement achievement = Achievement.builder().id(UUID.randomUUID()).build();
        AchievementProgress progress = AchievementProgress.builder().id(UUID.randomUUID()).build();
        AchievementProgressResponse expected = new AchievementProgressResponse(
                UUID.randomUUID(), "Achievement", "Description", AchievementType.SINGLE, null, null, false
        );

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(achievementService.getAchievementById(game, id)).thenReturn(achievement);
        when(achievementProgressService.completeAchievement(achievement)).thenReturn(progress);
        when(achievementProgressMapper.toResponse(progress)).thenReturn(expected);

        AchievementProgressResponse result = achievementProgressController.completeAchievement(id);

        assertEquals(expected, result);
        verify(gameService).getAuthenicatedGame();
        verify(achievementService).getAchievementById(game, id);
        verify(achievementProgressService).completeAchievement(achievement);
        verify(achievementProgressMapper).toResponse(progress);
    }

    @Test
    void increaseAchievementCount() {
        UUID id = UUID.randomUUID();
        Game game = Game.builder().id(UUID.randomUUID()).build();
        Achievement achievement = Achievement.builder().id(UUID.randomUUID()).build();
        AchievementProgress progress = AchievementProgress.builder().id(UUID.randomUUID()).build();
        AchievementProgressResponse expected = new AchievementProgressResponse(
                UUID.randomUUID(), "Achievement", "Description", AchievementType.SINGLE, null, null, false
        );

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(achievementService.getAchievementById(game, id)).thenReturn(achievement);
        when(achievementProgressService.increaseAchievementCount(1, achievement)).thenReturn(progress);
        when(achievementProgressMapper.toResponse(progress)).thenReturn(expected);

        AchievementProgressResponse result = achievementProgressController.increaseAchievementCount(id, 1);

        assertEquals(expected, result);
        verify(gameService).getAuthenicatedGame();
        verify(achievementService).getAchievementById(game, id);
        verify(achievementProgressService).increaseAchievementCount(1, achievement);
        verify(achievementProgressMapper).toResponse(progress);
    }

    @Test
    void decreaseAchievementCount() {
        UUID id = UUID.randomUUID();
        Game game = Game.builder().id(UUID.randomUUID()).build();
        Achievement achievement = Achievement.builder().id(UUID.randomUUID()).build();
        AchievementProgress progress = AchievementProgress.builder().id(UUID.randomUUID()).build();
        AchievementProgressResponse expected = new AchievementProgressResponse(
                UUID.randomUUID(), "Achievement", "Description", AchievementType.SINGLE, null, null, false
        );

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(achievementService.getAchievementById(game, id)).thenReturn(achievement);
        when(achievementProgressService.decreaseAchievementCount(1, achievement)).thenReturn(progress);
        when(achievementProgressMapper.toResponse(progress)).thenReturn(expected);

        AchievementProgressResponse result = achievementProgressController.decreaseAchievementCount(id, 1);

        assertEquals(expected, result);
        verify(gameService).getAuthenicatedGame();
        verify(achievementService).getAchievementById(game, id);
        verify(achievementProgressService).decreaseAchievementCount(1, achievement);
        verify(achievementProgressMapper).toResponse(progress);
    }
}
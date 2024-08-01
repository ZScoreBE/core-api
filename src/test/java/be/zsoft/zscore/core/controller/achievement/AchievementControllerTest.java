package be.zsoft.zscore.core.controller.achievement;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementMapper;
import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.dto.response.achievement.AchievementResponse;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.achievement.AchievementFixture;
import be.zsoft.zscore.core.fixtures.achievement.AchievementRequestFixture;
import be.zsoft.zscore.core.fixtures.achievement.AchievementResponseFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
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
class AchievementControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private AchievementController achievementController;

    @Test
    void createAchievement() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        AchievementRequest request = AchievementRequestFixture.aDefaultAchievementRequest();
        Achievement achievement = AchievementFixture.aDefaultAchievement();
        AchievementResponse expected = AchievementResponseFixture.aDefaultAchievementResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.createAchievement(game, request)).thenReturn(achievement);
        when(achievementMapper.toResponse(achievement)).thenReturn(expected);

        AchievementResponse result = achievementController.createAchievement(gameId, request);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(achievementService).createAchievement(game, request);
        verify(achievementMapper).toResponse(achievement);
    }

    @Test
    void getAchievements_all() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Achievement> achievements = new PageImpl<>(List.of(
                AchievementFixture.aDefaultAchievement(),
                AchievementFixture.aDefaultAchievement()
        ));
        Page<AchievementResponse> expected = new PageImpl<>(List.of(
                AchievementResponseFixture.aDefaultAchievementResponse(),
                AchievementResponseFixture.aDefaultAchievementResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.getAchievementsByGame(game,pageable)).thenReturn(achievements);
        when(achievementMapper.toResponse(achievements)).thenReturn(expected);

        PaginatedResponse<AchievementResponse> result = achievementController.getAchievements(gameId, null, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(achievementService).getAchievementsByGame(game, pageable);
        verify(achievementMapper).toResponse(achievements);
    }

    @Test
    void getAchievements_search() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Achievement> achievements = new PageImpl<>(List.of(
                AchievementFixture.aDefaultAchievement(),
                AchievementFixture.aDefaultAchievement()
        ));
        Page<AchievementResponse> expected = new PageImpl<>(List.of(
                AchievementResponseFixture.aDefaultAchievementResponse(),
                AchievementResponseFixture.aDefaultAchievementResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.searchAchievementsByGame("test", game,pageable)).thenReturn(achievements);
        when(achievementMapper.toResponse(achievements)).thenReturn(expected);

        PaginatedResponse<AchievementResponse> result = achievementController.getAchievements(gameId, "test", pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(achievementService).searchAchievementsByGame("test", game, pageable);
        verify(achievementMapper).toResponse(achievements);
    }

    @Test
    void getAchievement() {
        UUID gameId = UUID.randomUUID();
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Achievement achievement = AchievementFixture.aDefaultAchievement();
        AchievementResponse expected = AchievementResponseFixture.aDefaultAchievementResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.getAchievementById(game, achievementId)).thenReturn(achievement);
        when(achievementMapper.toResponse(achievement)).thenReturn(expected);

        AchievementResponse result = achievementController.getAchievement(gameId, achievementId);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(achievementService).getAchievementById(game, achievementId);
        verify(achievementMapper).toResponse(achievement);
    }

    @Test
    void updateAchievement() {
        UUID gameId = UUID.randomUUID();
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        UpdateAchievementRequest request = AchievementRequestFixture.aDefaultUpdateAchievementRequest();
        Achievement achievement = AchievementFixture.aDefaultAchievement();
        AchievementResponse expected = AchievementResponseFixture.aDefaultAchievementResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.updateAchievementById(game, achievementId, request)).thenReturn(achievement);
        when(achievementMapper.toResponse(achievement)).thenReturn(expected);

        AchievementResponse result = achievementController.updateAchievement(gameId, achievementId, request);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(achievementService).updateAchievementById(game, achievementId, request);
        verify(achievementMapper).toResponse(achievement);
    }

    @Test
    void deleteAchievement() {
        UUID gameId = UUID.randomUUID();
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(gameService.getById(gameId)).thenReturn(game);

        achievementController.deleteAchievement(gameId, achievementId);

        verify(gameService).getById(gameId);
        verify(achievementService).deleteAchievementById(game, achievementId);
    }

    @Test
    void countAchievements() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        CountResponse expected = new CountResponse(10L);

        when(gameService.getById(gameId)).thenReturn(game);
        when(achievementService.countAchievementsByGame(game)).thenReturn(10L);

        CountResponse result = achievementController.countAchievements(gameId);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(achievementService).countAchievementsByGame(game);
    }
}
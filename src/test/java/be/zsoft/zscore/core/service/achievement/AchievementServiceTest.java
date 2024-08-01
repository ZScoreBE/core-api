package be.zsoft.zscore.core.service.achievement;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementMapper;
import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.achievement.AchievementFixture;
import be.zsoft.zscore.core.fixtures.achievement.AchievementRequestFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.repository.achievement.AchievementRepo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepo achievementRepo;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void createAchievement() {
        Game game = GameFixture.aDefaultGame();
        AchievementRequest request = AchievementRequestFixture.aDefaultAchievementRequest();
        Achievement achievementFromMapper = AchievementFixture.aDefaultAchievement();
        Achievement expected = AchievementFixture.aDefaultAchievement();

        when(achievementMapper.fromRequest(request)).thenReturn(achievementFromMapper);
        when(achievementRepo.saveAndFlush(achievementFromMapper)).thenReturn(expected);

        Achievement result = achievementService.createAchievement(game, request);

        assertEquals(expected, result);

        verify(achievementMapper).fromRequest(request);
        verify(achievementRepo).saveAndFlush(achievementFromMapper);
    }

    @Test
    void getAchievementsByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Achievement> expected = new PageImpl<>(List.of(
                AchievementFixture.aDefaultAchievement(),
                AchievementFixture.aDefaultAchievement()
        ));

        when(achievementRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Achievement> result = achievementService.getAchievementsByGame(game, pageable);

        assertEquals(expected, result);

        verify(achievementRepo).findAllByGame(game, pageable);
    }

    @Test
    void searchAchievementsByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Achievement> expected = new PageImpl<>(List.of(
                AchievementFixture.aDefaultAchievement(),
                AchievementFixture.aDefaultAchievement()
        ));

        when(achievementRepo.searchAllOnNameByGame("%test%", game, pageable)).thenReturn(expected);

        Page<Achievement> result = achievementService.searchAchievementsByGame("TeSt", game, pageable);

        assertEquals(expected, result);

        verify(achievementRepo).searchAllOnNameByGame("%test%", game, pageable);
    }

    @Test
    void getAchievementById_success() {
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Achievement expected = AchievementFixture.aDefaultAchievement();

        when(achievementRepo.findByIdAndGame(achievementId, game)).thenReturn(Optional.of(expected));

        Achievement result = achievementService.getAchievementById(game, achievementId);

        assertEquals(expected, result);
    }

    @Test
    void getAchievementById_notFound() {
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(achievementRepo.findByIdAndGame(achievementId, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> achievementService.getAchievementById(game, achievementId));
    }

    @Test
    void updateAchievementById() {
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Achievement expected = AchievementFixture.aDefaultAchievement();
        UpdateAchievementRequest request = AchievementRequestFixture.aDefaultUpdateAchievementRequest();

        when(achievementRepo.findByIdAndGame(achievementId, game)).thenReturn(Optional.of(expected));
        when(achievementMapper.fromRequest(request, expected)).thenReturn(expected);
        when(achievementRepo.saveAndFlush(expected)).thenReturn(expected);

        Achievement result = achievementService.updateAchievementById(game, achievementId, request);

        assertEquals(expected, result);

        verify(achievementRepo).findByIdAndGame(achievementId, game);
        verify(achievementMapper).fromRequest(request, expected);
        verify(achievementRepo).saveAndFlush(expected);
    }

    @Test
    void deleteAchievementById() {
        UUID achievementId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Achievement achievement = AchievementFixture.aDefaultAchievement();

        when(achievementRepo.findByIdAndGame(achievementId, game)).thenReturn(Optional.of(achievement));

        achievementService.deleteAchievementById(game, achievementId);

        verify(achievementRepo).delete(achievement);
    }

    @Test
    void getAllAchievementsByGame() {
        Game game = GameFixture.aDefaultGame();
        List<Achievement> expected = List.of(
                AchievementFixture.aDefaultAchievement(),
                AchievementFixture.aDefaultAchievement()
        );

        when(achievementRepo.findAllByGame(game)).thenReturn(expected);

        List<Achievement> result = achievementService.getAllAchievementsByGame(game);

        assertEquals(expected, result);
        verify(achievementRepo).findAllByGame(game);
    }

    @Test
    void countAchievementsByGame() {
        Game game = GameFixture.aDefaultGame();
        long expected = 10;

        when(achievementRepo.countByGame(game)).thenReturn(expected);

        long result = achievementService.countAchievementsByGame(game);

        assertEquals(expected, result);
        verify(achievementRepo).countByGame(game);
    }
}
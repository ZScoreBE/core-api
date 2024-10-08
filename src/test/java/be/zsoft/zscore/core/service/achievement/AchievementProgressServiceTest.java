package be.zsoft.zscore.core.service.achievement;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.achievement.AchievementFixture;
import be.zsoft.zscore.core.fixtures.achievement.AchievementProgressFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.repository.achievement.AchievementProgressRepo;
import be.zsoft.zscore.core.service.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementProgressServiceTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementProgressRepo achievementProgressRepo;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private AchievementProgressService achievementProgressService;

    @Captor
    private ArgumentCaptor<List<AchievementProgress>> progressesCaptor;

    @Captor
    private ArgumentCaptor<AchievementProgress> progressCaptor;

    @Test
    void createAchievementProgressesForNewPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement1 = AchievementFixture.aMultipleAchievement();
        Achievement achievement2 = AchievementFixture.aSingleAchievement();

        when(achievementService.getAllAchievementsByGame(player.getGame())).thenReturn(List.of(achievement1, achievement2));

        achievementProgressService.createAchievementProgressesForNewPlayer(player);

        verify(achievementProgressRepo).saveAllAndFlush(progressesCaptor.capture());

        assertFalse(progressesCaptor.getValue().get(0).isCompleted());
        assertEquals(0, progressesCaptor.getValue().get(0).getCurrentCount());
        assertEquals(achievement1, progressesCaptor.getValue().get(0).getAchievement());
        assertEquals(player, progressesCaptor.getValue().get(0).getPlayer());

        assertFalse(progressesCaptor.getValue().get(1).isCompleted());
        assertNull(progressesCaptor.getValue().get(1).getCurrentCount());
        assertEquals(achievement2, progressesCaptor.getValue().get(1).getAchievement());
        assertEquals(player, progressesCaptor.getValue().get(1).getPlayer());
    }

    @Test
    void completeAchievement_success() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aDefaultAchievement();
        AchievementProgress expected = AchievementProgressFixture.aDefaultAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(expected));
        when(achievementProgressRepo.saveAndFlush(any(AchievementProgress.class))).thenReturn(expected);

        AchievementProgress result = achievementProgressService.completeAchievement(achievement);

        verify(achievementProgressRepo).saveAndFlush(progressCaptor.capture());

        assertEquals(expected, result);
        assertTrue(progressCaptor.getValue().isCompleted());
    }

    @Test
    void completeAchievement_notFound() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aDefaultAchievement();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> achievementProgressService.completeAchievement(achievement));

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void increaseAchievementCount_singleType() {
        Achievement achievement = AchievementFixture.aSingleAchievement();

        ApiException ex = assertThrows(ApiException.class, () -> achievementProgressService.increaseAchievementCount(1, achievement));

        assertEquals(ErrorCodes.ACHIEVEMENT_COUNT_INCREASE_SHOULD_HAVE_TYPE_MULTIPLE, ex.getErrorKey());

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void increaseAchievementCount_notFound() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> achievementProgressService.increaseAchievementCount(1, achievement));

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void increaseAchievementCount_alreadyCompleted() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();
        AchievementProgress progress = AchievementProgressFixture.aCompletedAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(progress));

        ApiException ex = assertThrows(ApiException.class, () -> achievementProgressService.increaseAchievementCount(1, achievement));

        assertEquals(ErrorCodes.ACHIEVEMENT_ALREADY_COMPLETED, ex.getErrorKey());

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void increaseAchievementCount_newCountGreaterThanNeededCount() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();
        AchievementProgress expected = AchievementProgressFixture.aDefaultAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(expected));
        when(achievementProgressRepo.saveAndFlush(any(AchievementProgress.class))).thenReturn(expected);

        AchievementProgress result = achievementProgressService.increaseAchievementCount(30, achievement);

        verify(achievementProgressRepo).saveAndFlush(progressCaptor.capture());

        assertEquals(expected, result);
        assertTrue(progressCaptor.getValue().isCompleted());
        assertEquals(20, progressCaptor.getValue().getCurrentCount());
    }

    @Test
    void increaseAchievementCount_normalIncrease() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aDefaultAchievement();
        AchievementProgress expected = AchievementProgressFixture.aDefaultAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(expected));
        when(achievementProgressRepo.saveAndFlush(any(AchievementProgress.class))).thenReturn(expected);

        AchievementProgress result = achievementProgressService.increaseAchievementCount(2, achievement);

        verify(achievementProgressRepo).saveAndFlush(progressCaptor.capture());

        assertEquals(expected, result);
        assertFalse(progressCaptor.getValue().isCompleted());
        assertEquals(12, progressCaptor.getValue().getCurrentCount());
    }

    @Test
    void decreaseAchievementCount_singleType() {
        Achievement achievement = AchievementFixture.aSingleAchievement();

        ApiException ex = assertThrows(ApiException.class, () -> achievementProgressService.decreaseAchievementCount(1, achievement));

        assertEquals(ErrorCodes.ACHIEVEMENT_COUNT_DECREASE_SHOULD_HAVE_TYPE_MULTIPLE, ex.getErrorKey());

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void decreaseAchievementCount_notFound() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> achievementProgressService.decreaseAchievementCount(1, achievement));

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void decreaseAchievementCount_alreadyCompleted() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();
        AchievementProgress progress = AchievementProgressFixture.aCompletedAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(progress));

        ApiException ex = assertThrows(ApiException.class, () -> achievementProgressService.decreaseAchievementCount(1, achievement));

        assertEquals(ErrorCodes.ACHIEVEMENT_ALREADY_COMPLETED, ex.getErrorKey());

        verify(achievementProgressRepo, never()).saveAndFlush(any(AchievementProgress.class));
    }

    @Test
    void decreaseAchievementCount_newCountSmallerThanZero() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();
        AchievementProgress expected = AchievementProgressFixture.aDefaultAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(expected));
        when(achievementProgressRepo.saveAndFlush(any(AchievementProgress.class))).thenReturn(expected);

        AchievementProgress result = achievementProgressService.decreaseAchievementCount(30, achievement);

        verify(achievementProgressRepo).saveAndFlush(progressCaptor.capture());

        assertEquals(expected, result);
        assertEquals(0, progressCaptor.getValue().getCurrentCount());
    }

    @Test
    void decreaseAchievementCount_normalDecrease() {
        Player player = PlayerFixture.aDefaultPlayer();
        Achievement achievement = AchievementFixture.aMultipleAchievement();
        AchievementProgress expected = AchievementProgressFixture.aDefaultAchievementProgress();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findByAchievementAndPlayer(achievement, player)).thenReturn(Optional.of(expected));
        when(achievementProgressRepo.saveAndFlush(any(AchievementProgress.class))).thenReturn(expected);

        AchievementProgress result = achievementProgressService.decreaseAchievementCount(5, achievement);

        verify(achievementProgressRepo).saveAndFlush(progressCaptor.capture());

        assertEquals(expected, result);
        assertEquals(5, progressCaptor.getValue().getCurrentCount());
    }

    @Test
    void getAchievementProgresses() {
        Player player = PlayerFixture.aDefaultPlayer();
        Pageable pageable = PageRequest.of(1, 10);
        Page<AchievementProgress> expected = new PageImpl<>(List.of(
                AchievementProgressFixture.aDefaultAchievementProgress(),
                AchievementProgressFixture.aDefaultAchievementProgress()
        ));

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(achievementProgressRepo.findAllByPlayer(player, pageable)).thenReturn(expected);

        Page<AchievementProgress> result = achievementProgressService.getAchievementProgresses(pageable);

        assertEquals(expected, result);
        verify(playerService).getAuthenticatedPlayer();
        verify(achievementProgressRepo).findAllByPlayer(player, pageable);
    }

    @Test
    void deleteAllProgressesByPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();

        achievementProgressService.deleteAllProgressesByPlayer(player);

        verify(achievementProgressRepo).deleteAllByPlayer(player);
    }
}
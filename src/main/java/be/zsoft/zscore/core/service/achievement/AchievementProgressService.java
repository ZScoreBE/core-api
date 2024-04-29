package be.zsoft.zscore.core.service.achievement;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.repository.achievement.AchievementProgressRepo;
import be.zsoft.zscore.core.service.player.PlayerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AchievementProgressService {

    private final AchievementService achievementService;
    private final AchievementProgressRepo achievementProgressRepo;
    private final PlayerService playerService;

    @Transactional
    public void createAchievementProgressesForNewPlayer(Player player) {
        List<AchievementProgress> progresses = achievementService.getAllAchievementsByGame(player.getGame()).stream()
                .map(achievement -> createAchievementProgress(achievement, player))
                .toList();

        achievementProgressRepo.saveAllAndFlush(progresses);
    }

    public AchievementProgress completeAchievement(Achievement achievement) {
        AchievementProgress progress = getAchievementProgressByAchievementAndPlayer(achievement, playerService.getAuthenticatedPlayer());

        progress.setCompleted(true);
        return achievementProgressRepo.saveAndFlush(progress);
    }

    public AchievementProgress increaseAchievementCount(int amount, Achievement achievement) {
        if (achievement.getType() == AchievementType.SINGLE) {
            throw new ApiException(ErrorCodes.ACHIEVEMENT_COUNT_INCREASE_SHOULD_HAVE_TYPE_MULTIPLE);
        }

        AchievementProgress progress = getAchievementProgressByAchievementAndPlayer(achievement, playerService.getAuthenticatedPlayer());

        if (progress.isCompleted()) {
            throw new ApiException(ErrorCodes.ACHIEVEMENT_ALREADY_COMPLETED);
        }

        int newCount = progress.getCurrentCount() + amount;
        if (newCount >= achievement.getNeededCount()) {
            progress.setCurrentCount(achievement.getNeededCount());
            progress.setCompleted(true);
        } else {
            progress.setCurrentCount(newCount);
        }

        return achievementProgressRepo.saveAndFlush(progress);
    }

    public AchievementProgress decreaseAchievementCount(int amount, Achievement achievement) {
        if (achievement.getType() == AchievementType.SINGLE) {
            throw new ApiException(ErrorCodes.ACHIEVEMENT_COUNT_DECREASE_SHOULD_HAVE_TYPE_MULTIPLE);
        }

        AchievementProgress progress = getAchievementProgressByAchievementAndPlayer(achievement, playerService.getAuthenticatedPlayer());

        if (progress.isCompleted()) {
            throw new ApiException(ErrorCodes.ACHIEVEMENT_ALREADY_COMPLETED);
        }

        int newCount = progress.getCurrentCount() - amount;
        progress.setCurrentCount(Math.max(newCount, 0));

        return achievementProgressRepo.saveAndFlush(progress);
    }

    public Page<AchievementProgress> getAchievementProgresses(Pageable pageable) {
        return achievementProgressRepo.findAllByPlayer(playerService.getAuthenticatedPlayer(), pageable);
    }

    private AchievementProgress createAchievementProgress(Achievement achievement, Player player) {
        return AchievementProgress.builder()
                .completed(false)
                .currentCount(achievement.getType() == AchievementType.MULTIPLE ? 0 : null)
                .achievement(achievement)
                .player(player)
                .build();
    }

    private AchievementProgress getAchievementProgressByAchievementAndPlayer(Achievement achievement, Player player) {
        return achievementProgressRepo.findByAchievementAndPlayer(achievement, player)
                .orElseThrow(() -> new NotFoundException("Could not find any achievement progress for achievement '%s' and player '%s'".formatted(achievement.getId(), player.getId())));
    }

    public void deleteAllProgressesByPlayer(Player player) {
        achievementProgressRepo.deleteAllByPlayer(player);
    }
}

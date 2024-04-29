package be.zsoft.zscore.core.service.achievement;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.achievement.AchievementMapper;
import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.repository.achievement.AchievementRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AchievementService {

    private final AchievementMapper achievementMapper;
    private final AchievementRepo achievementRepo;

    public Achievement createAchievement(Game game, AchievementRequest request) {
        Achievement achievement = achievementMapper.fromRequest(request);
        achievement.setGame(game);

        return achievementRepo.saveAndFlush(achievement);
    }

    public List<Achievement> getAllAchievementsByGame(Game game) {
        return achievementRepo.findAllByGame(game);
    }

    public Page<Achievement> getAchievementsByGame(Game game, Pageable pageable) {
        return achievementRepo.findAllByGame(game, pageable);
    }

    public Page<Achievement> searchAchievementsByGame(String search, Game game, Pageable pageable) {
        return achievementRepo.searchAllOnNameByGame("%" + search.toLowerCase() + "%", game, pageable);
    }

    public Achievement getAchievementById(Game game, UUID id) {
        return achievementRepo.findByIdAndGame(id, game)
                .orElseThrow(() -> new NotFoundException("Could not find achievement '%s' for game '%s'".formatted(id, game.getId())));
    }

    public Achievement updateAchievementById(Game game, UUID id, UpdateAchievementRequest request) {
        Achievement achievement = achievementMapper.fromRequest(request, getAchievementById(game, id));

        return achievementRepo.saveAndFlush(achievement);
    }

    public void deleteAchievementById(Game game, UUID id) {
        Achievement achievement = getAchievementById(game, id);
        achievementRepo.delete(achievement);
    }

    public long countAchievementsByGame(Game game) {
        return achievementRepo.countByGame(game);
    }
}

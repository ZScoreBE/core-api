package be.zsoft.zscore.core.service.leaderboard;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.repository.leaderboard.LeaderboardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LeaderboardService {

    private final LeaderboardMapper leaderboardMapper;
    private final LeaderboardRepo leaderboardRepo;
    private final LeaderboardScoreService leaderboardScoreService;

    public Leaderboard createLeaderboard(Game game, LeaderboardRequest request) {
        Leaderboard leaderboard = leaderboardMapper.fromRequest(request);
        leaderboard.setGame(game);

        return leaderboardRepo.saveAndFlush(leaderboard);
    }

    public Leaderboard getLeaderboardById(Game game, UUID id) {
        return leaderboardRepo.findByIdAndGame(id, game)
                .orElseThrow(() -> new NotFoundException("Could not find leaderboard '%s' for game '%s'".formatted(id, game.getId())));
    }

    public Leaderboard updateLeaderboard(Game game, UUID id, LeaderboardRequest request) {
        Leaderboard leaderboard = leaderboardMapper.fromRequest(request, getLeaderboardById(game, id));

        return leaderboardRepo.saveAndFlush(leaderboard);
    }

    @Transactional
    public void deleteLeaderboard(Game game, UUID id) {
        Leaderboard leaderboard = getLeaderboardById(game, id);

        leaderboardScoreService.deleteAllScores(leaderboard);
        leaderboardRepo.delete(leaderboard);
    }

    public Page<Leaderboard> getLeaderboardsByGame(Game game, Pageable pageable) {
        return leaderboardRepo.findAllByGame(game, pageable);
    }

    public Page<Leaderboard> searchLeaderboardsByGame(String search, Game game, Pageable pageable) {
        return leaderboardRepo.searchOnNameAllByGame("%" + search.toLowerCase() + "%", game, pageable);
    }

    public long countLeaderboardsByGame(Game game) {
        return leaderboardRepo.countByGame(game);
    }
}

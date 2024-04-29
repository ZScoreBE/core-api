package be.zsoft.zscore.core.service.leaderboard;

import be.zsoft.zscore.core.dto.request.leaderboard.ExternalLeaderboardScoreRequest;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardScoreRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.repository.leaderboard.LeaderboardScoreRepo;
import be.zsoft.zscore.core.service.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeaderboardScoreService {

    private final LeaderboardScoreRepo leaderboardScoreRepo;
    private final PlayerService playerService;

    public LeaderboardScore createLeaderboardScore(Leaderboard leaderboard, ExternalLeaderboardScoreRequest request) {
        Player player = playerService.getAuthenticatedPlayer();

        return switch (leaderboard.getScoreType()) {
            case HIGHEST -> saveLeaderboardScoreForHighest(leaderboard, player, request.score());
            case LATEST -> saveLeaderboardScoreForLatest(leaderboard, player, request.score());
            case MULTIPLE -> saveNewScore(leaderboard, player, request.score());
        };
    }

    public LeaderboardScore createLeaderboardScore(Game game, Leaderboard leaderboard, LeaderboardScoreRequest request) {
        Player player = playerService.getPlayerByIdAndGame(request.playerId(), game);

        return switch (leaderboard.getScoreType()) {
            case HIGHEST -> saveLeaderboardScoreForHighest(leaderboard, player, request.score());
            case LATEST -> saveLeaderboardScoreForLatest(leaderboard, player, request.score());
            case MULTIPLE -> saveNewScore(leaderboard, player, request.score());
        };
    }

    public Page<LeaderboardScore> getScores(Leaderboard leaderboard, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                leaderboard.getDirection(),
                "score"
        );
        return leaderboardScoreRepo.findAllScoresByLeaderboard(leaderboard, sortedPageable);
    }

    public void deleteAllScores(Leaderboard leaderboard) {
        leaderboardScoreRepo.deleteAllByLeaderboard(leaderboard);
    }

    private LeaderboardScore saveLeaderboardScoreForLatest(Leaderboard leaderboard, Player player, int score) {
        return leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)
                .map(leaderboardScore -> updateScore(leaderboardScore, score))
                .orElseGet(() -> saveNewScore(leaderboard, player, score));
    }

    private LeaderboardScore saveLeaderboardScoreForHighest(Leaderboard leaderboard, Player player, int score) {
        Optional<LeaderboardScore> leaderboardScore = leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player);

        if (leaderboardScore.isEmpty()) {
            return saveNewScore(leaderboard, player, score);
        }

        LeaderboardScore leaderboardScoreValue = leaderboardScore.get();

        if (leaderboardScoreValue.getScore() < score) {
            return updateScore(leaderboardScore.get(), score);
        }

        return leaderboardScoreValue;
    }

    private LeaderboardScore updateScore(LeaderboardScore leaderboardScore, int score) {
        leaderboardScore.setScore(score);
        return leaderboardScoreRepo.saveAndFlush(leaderboardScore);
    }

    private LeaderboardScore saveNewScore(Leaderboard leaderboard, Player player, int score) {
        LeaderboardScore leaderboardScore = LeaderboardScore.builder()
                .score(score)
                .player(player)
                .leaderboard(leaderboard)
                .build();

        return leaderboardScoreRepo.saveAndFlush(leaderboardScore);
    }

    public void deleteAllScoresByPlayer(Player player) {
        leaderboardScoreRepo.deleteAllByPlayer(player);
    }
}

package be.zsoft.zscore.core.controller.external.leaderboard;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardScoreMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.ExternalLeaderboardScoreRequest;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardScoreService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/leaderboards")
public class ExternalLeaderboardController {

    private final GameService gameService;
    private final LeaderboardService leaderboardService;
    private final LeaderboardScoreService leaderboardScoreService;
    private final LeaderboardMapper leaderboardMapper;
    private final LeaderboardScoreMapper leaderboardScoreMapper;

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<LeaderboardResponse> getLeaderboards(Pageable pageable) {
        Game game = gameService.getAuthenicatedGame();
        Page<Leaderboard> leaderboards = leaderboardService.getLeaderboardsByGame(game, pageable);

        return PaginatedResponse.createResponse(
                leaderboardMapper.toResponse(leaderboards),
                "/external/leaderboards"
        );
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public LeaderboardResponse getLeaderboard(@PathVariable UUID id) {
        Game game = gameService.getAuthenicatedGame();
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(game, id);

        return leaderboardMapper.toResponse(leaderboard);
    }

    @Secured({"ROLE_PLAYER"})
    @PostMapping("/{id}/scores")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public LeaderboardScoreResponse createLeaderboardScore(
            @PathVariable UUID id,
            @RequestBody @Valid ExternalLeaderboardScoreRequest request
    ) {
        Game game = gameService.getAuthenicatedGame();
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(game, id);
        LeaderboardScore score = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        return leaderboardScoreMapper.toResponse(score);
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping("/{id}/scores")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<LeaderboardScoreResponse> getLeaderboardScores(@PathVariable UUID id, Pageable pageable) {
        Game game = gameService.getAuthenicatedGame();
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(game, id);
        Page<LeaderboardScore> scores = leaderboardScoreService.getScores(leaderboard, pageable);

        return PaginatedResponse.createResponse(
                leaderboardScoreMapper.toResponse(scores),
                "/external/leaderboards/%s/scores".formatted(id)
        );
    }
}

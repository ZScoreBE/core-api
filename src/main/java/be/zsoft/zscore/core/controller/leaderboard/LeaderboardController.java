package be.zsoft.zscore.core.controller.leaderboard;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardScoreMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games/{gameId}/leaderboards")
public class LeaderboardController {

    private final GameService gameService;
    private final LeaderboardService leaderboardService;
    private final LeaderboardScoreService leaderboardScoreService;
    private final LeaderboardMapper leaderboardMapper;
    private final LeaderboardScoreMapper leaderboardScoreMapper;

    @Secured({"ROLE_USER"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LeaderboardResponse createLeaderboard(
            @PathVariable UUID gameId,
            @RequestBody @Valid LeaderboardRequest leaderboardRequest
    ) {
        Game game = gameService.getById(gameId);
        Leaderboard leaderboard = leaderboardService.createLeaderboard(game, leaderboardRequest);

        return leaderboardMapper.toResponse(leaderboard);
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PaginatedResponse<LeaderboardResponse> getLeaderboards(
            @PathVariable UUID gameId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable) {
        Game game = gameService.getById(gameId);
        Page<Leaderboard> leaderboards = StringUtils.hasText(search) ?
                leaderboardService.searchLeaderboardsByGame(search, game, pageable) :
                leaderboardService.getLeaderboardsByGame(game, pageable);

        return PaginatedResponse.createResponse(
                leaderboardMapper.toResponse(leaderboards),
                "/games/%s/leaderboards".formatted(gameId)
        );
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/count")
    public CountResponse countLeaderboards(@PathVariable("gameId") UUID gameId) {
        Game game = gameService.getById(gameId);
        long leaderboardCount = leaderboardService.countLeaderboardsByGame(game);

        return new CountResponse(leaderboardCount);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LeaderboardResponse getLeaderboard(
            @PathVariable UUID gameId,
            @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(game, id);

        return leaderboardMapper.toResponse(leaderboard);
    }

    @Secured({"ROLE_USER"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LeaderboardResponse updateLeaderboard(
            @PathVariable UUID gameId,
            @PathVariable UUID id,
            @RequestBody @Valid LeaderboardRequest leaderboardRequest
    ) {
        Game game = gameService.getById(gameId);
        Leaderboard leaderboard = leaderboardService.updateLeaderboard(game, id, leaderboardRequest);

        return leaderboardMapper.toResponse(leaderboard);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/{id}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PaginatedResponse<LeaderboardScoreResponse> getLeaderboardScores(
            @PathVariable UUID gameId,
            @PathVariable UUID id,
            Pageable pageable) {
        Game game = gameService.getById(gameId);
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(game, id);
        Page<LeaderboardScore> scores = leaderboardScoreService.getScores(leaderboard, pageable);

        return PaginatedResponse.createResponse(
                leaderboardScoreMapper.toResponse(scores),
                "/games/%s/leaderboards/%s/scores".formatted(gameId, id)
        );
    }

    @Secured({"ROLE_USER"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteLeaderboard(
            @PathVariable UUID gameId,
            @PathVariable UUID id
    ) {
        Game game = gameService.getById(gameId);
        leaderboardService.deleteLeaderboard(game, id);
    }
}

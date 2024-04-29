package be.zsoft.zscore.core.controller.player;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardScoreService;
import be.zsoft.zscore.core.service.player.PlayerService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/games/{gameId}/players")
public class PlayerController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    private final AchievementProgressService achievementProgressService;
    private final LeaderboardScoreService leaderboardScoreService;

    @Secured({"ROLE_USER"})
    @GetMapping
    public PaginatedResponse<PlayerResponse> getPlayers(
            @PathVariable("gameId") UUID gameId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable) {
        Game game = gameService.getById(gameId);
        Page<Player> players = StringUtils.hasText(search) ?
                playerService.searchPlayersByGame(search, game, pageable) :
                playerService.getPlayersByGame(game, pageable);

        return PaginatedResponse.createResponse(playerMapper.toResponse(players), "/games/%s/players".formatted(gameId));
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/count")
    public CountResponse countPlayers(@PathVariable("gameId") UUID gameId) {
        Game game = gameService.getById(gameId);
        long playerCount = playerService.countPlayersByGame(game);

        return new CountResponse(playerCount);
    }

    @Secured({"ROLE_USER"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deletePlayer(@PathVariable UUID gameId, @PathVariable UUID id) {
        Game game = gameService.getById(gameId);
        Player player = playerService.getPlayerByIdAndGame(id, game);

        achievementProgressService.deleteAllProgressesByPlayer(player);
        leaderboardScoreService.deleteAllScoresByPlayer(player);

        playerService.deletePlayer(player);
    }



}

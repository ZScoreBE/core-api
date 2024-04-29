package be.zsoft.zscore.core.controller.game;

import be.zsoft.zscore.core.dto.mapper.game.GameMapper;
import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.dto.response.common.BoolResultResponse;
import be.zsoft.zscore.core.dto.response.game.GameResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameMapper gameMapper;
    private final GameService gameService;

    @Secured({"ROLE_USER"})
    @PostMapping
    public GameResponse createGame(@RequestBody @Valid GameRequest request) {
        Game sandboxGame = gameService.createGames(request);
        return gameMapper.toResponse(sandboxGame);
    }

    @Secured({"ROLE_USER"})
    @GetMapping
    public List<GameResponse> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return gameMapper.toResponse(games);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/has-games")
    public BoolResultResponse hasGames() {
        boolean hasGames = gameService.hasGames();
        return new BoolResultResponse(hasGames);
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/{id}")
    @ResponseBody
    public GameResponse getGame(@PathVariable UUID id) {
        Game game = gameService.getById(id);
        return gameMapper.toResponse(game);
    }

    @Secured({"ROLE_USER"})
    @PutMapping("/{id}")
    @ResponseBody
    public GameResponse updateGame(@PathVariable UUID id, @RequestBody @Valid GameRequest request) {
        Game game = gameService.updateGameGeneralSettings(id, request);
        return gameMapper.toResponse(game);
    }

    @Secured({"ROLE_USER"})
    @PatchMapping("/{id}/regenerate-api-key")
    @ResponseBody
    public GameResponse regenerateApiKey(@PathVariable UUID id) {
        Game game = gameService.regenerateApiKey(id);

        return gameMapper.toResponse(game);
    }
}

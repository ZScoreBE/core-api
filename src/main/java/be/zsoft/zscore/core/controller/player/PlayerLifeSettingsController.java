package be.zsoft.zscore.core.controller.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerLifeSettingsMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerLifeSettingsResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerLifeSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games/{gameId}/player-life-settings")
public class PlayerLifeSettingsController {

    private final PlayerLifeSettingsMapper playerLifeSettingsMapper;
    private final PlayerLifeSettingsService playerLifeSettingsService;
    private final GameService gameService;

    @Secured({"ROLE_USER"})
    @GetMapping
    @ResponseBody
    public PlayerLifeSettingsResponse getPlayerLifeSettings(@PathVariable UUID gameId) {
        Game game = gameService.getById(gameId);
        PlayerLifeSettings settings = playerLifeSettingsService.getPlayerLifeSettings(game);

        return playerLifeSettingsMapper.toResponse(settings);
    }

    @Secured({"ROLE_USER"})
    @PutMapping
    @ResponseBody
    public PlayerLifeSettingsResponse updatePlayerLifeSettings(
            @PathVariable UUID gameId, @Valid @RequestBody PlayerLifeSettingsRequest request
    ) {
        Game game = gameService.getById(gameId);
        PlayerLifeSettings settings = playerLifeSettingsService.updatePlayerLifeSettings(request, game);

        return playerLifeSettingsMapper.toResponse(settings);
    }
}

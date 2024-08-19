package be.zsoft.zscore.core.controller.external.trigger;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.trigger.TriggerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/triggers")
public class ExternalTriggerController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final TriggerService triggerService;
    private final TriggerMapper triggerMapper;

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    public PaginatedResponse<TriggerResponse> getTriggers(Pageable pageable) {
        Game game = gameService.getAuthenicatedGame();
        Page<Trigger> triggers = triggerService.getTriggersByGame(game, pageable);

        return PaginatedResponse.createResponse(
                triggerMapper.toResponse(triggers),
                "/external/triggers"
        );
    }

    @Secured({"ROLE_PLAYER"})
    @PostMapping("/{id}/execute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void executeTrigger(@PathVariable UUID id) {
        Player player = playerService.getAuthenticatedPlayer();
        triggerService.executeTrigger(id, player);
    }
}

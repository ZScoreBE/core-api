package be.zsoft.zscore.core.controller.trigger;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.trigger.TriggerService;
import be.zsoft.zscore.core.validation.request.TriggerValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games/{gameId}/triggers")
public class TriggerController {

    private final GameService gameService;
    private final TriggerService triggerService;
    private final TriggerMapper triggerMapper;
    private final TriggerValidator triggerValidator;

    @PostMapping
    @Secured({"ROLE_USER"})
    public TriggerResponse createTrigger(
            @PathVariable UUID gameId, @Valid @RequestBody TriggerRequest request
    ) {
        triggerValidator.validate(request);

        Game game = gameService.getById(gameId);
        Trigger trigger = triggerService.createTrigger(request, game);

        return triggerMapper.toResponse(trigger);
    }

    @GetMapping
    @Secured({"ROLE_USER"})
    public PaginatedResponse<TriggerResponse> getTriggers(
            @PathVariable UUID gameId,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Game game = gameService.getById(gameId);

        Page<Trigger> triggers = StringUtils.hasText(search) ?
                triggerService.searchTriggersByGame(search, game, pageable) :
                triggerService.getTriggersByGame(game, pageable);

        return PaginatedResponse.createResponse(
                triggerMapper.toResponse(triggers),
                "/games/%s/triggers".formatted(gameId)
        );
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER"})
    public TriggerResponse getTrigger(@PathVariable UUID gameId, @PathVariable UUID id) {
        Game game = gameService.getById(gameId);
        Trigger trigger = triggerService.getTriggerByIdAndGame(id, game);

        return triggerMapper.toResponse(trigger);
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_USER"})
    public TriggerResponse updateTrigger
            (@PathVariable UUID gameId, @PathVariable UUID id, @Valid @RequestBody TriggerRequest request) {
        triggerValidator.validate(request);

        Game game = gameService.getById(gameId);
        Trigger trigger = triggerService.updateTrigger(request, id, game);

        return triggerMapper.toResponse(trigger);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_USER"})
    public void deleteTrigger(@PathVariable UUID gameId, @PathVariable UUID id) {
        Game game = gameService.getById(gameId);
        triggerService.deleteByIdAndGame(id, game);
    }
}

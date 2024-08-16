package be.zsoft.zscore.core.controller.trigger;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerRequestFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerResponseFixture;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.trigger.TriggerService;
import be.zsoft.zscore.core.validation.request.TriggerValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TriggerControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private TriggerService triggerService;

    @Mock
    private TriggerMapper triggerMapper;

    @Mock
    private TriggerValidator triggerValidator;

    @InjectMocks
    private TriggerController triggerController;

    @Test
    void createTrigger() {
        UUID gameId = UUID.randomUUID();
        TriggerRequest request = TriggerRequestFixture.aDefaultTriggerRequest();
        Game game = GameFixture.aDefaultGame();
        Trigger trigger = TriggerFixture.aDefaultTrigger();
        TriggerResponse expected = TriggerResponseFixture.aDefaultTriggerResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(triggerService.createTrigger(request, game)).thenReturn(trigger);
        when(triggerMapper.toResponse(trigger)).thenReturn(expected);

        TriggerResponse result = triggerController.createTrigger(gameId, request);

        verify(triggerValidator).validate(request);
        verify(gameService).getById(gameId);
        verify(triggerService).createTrigger(request, game);
        verify(triggerMapper).toResponse(trigger);

        assertEquals(expected, result);
    }

    @Test
    void getTriggers_find() {
        UUID gameId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(1, 10);
        Game game = GameFixture.aDefaultGame();
        Page<Trigger> triggers = new PageImpl<>(List.of(
                TriggerFixture.aDefaultTrigger(), TriggerFixture.aDefaultTrigger()
        ));
        List<TriggerResponse> expected = List.of(
                TriggerResponseFixture.aDefaultTriggerResponse(), TriggerResponseFixture.aDefaultTriggerResponse()
        );

        when(gameService.getById(gameId)).thenReturn(game);
        when(triggerService.getTriggersByGame(game, pageable)).thenReturn(triggers);
        when(triggerMapper.toResponse(triggers)).thenReturn(new PageImpl<>(expected));

        PaginatedResponse<TriggerResponse> result = triggerController.getTriggers(gameId, null, pageable);

        verify(gameService).getById(gameId);
        verify(triggerService).getTriggersByGame(game, pageable);
        verify(triggerMapper).toResponse(triggers);

        assertEquals(expected, result.items());
    }

    @Test
    void getTriggers_search() {
        UUID gameId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(1, 10);
        Game game = GameFixture.aDefaultGame();
        Page<Trigger> triggers = new PageImpl<>(List.of(
                TriggerFixture.aDefaultTrigger(), TriggerFixture.aDefaultTrigger()
        ));
        List<TriggerResponse> expected = List.of(
                TriggerResponseFixture.aDefaultTriggerResponse(), TriggerResponseFixture.aDefaultTriggerResponse()
        );

        when(gameService.getById(gameId)).thenReturn(game);
        when(triggerService.searchTriggersByGame("search", game, pageable)).thenReturn(triggers);
        when(triggerMapper.toResponse(triggers)).thenReturn(new PageImpl<>(expected));

        PaginatedResponse<TriggerResponse> result = triggerController.getTriggers(gameId, "search", pageable);

        verify(gameService).getById(gameId);
        verify(triggerService).searchTriggersByGame("search", game, pageable);
        verify(triggerMapper).toResponse(triggers);

        assertEquals(expected, result.items());
    }

    @Test
    void getTrigger() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Trigger trigger = TriggerFixture.aDefaultTrigger();
        TriggerResponse expected = TriggerResponseFixture.aDefaultTriggerResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(triggerService.getTriggerByIdAndGame(id, game)).thenReturn(trigger);
        when(triggerMapper.toResponse(trigger)).thenReturn(expected);

        TriggerResponse result = triggerController.getTrigger(gameId, id);

        verify(gameService).getById(gameId);
        verify(triggerService).getTriggerByIdAndGame(id, game);
        verify(triggerMapper).toResponse(trigger);

        assertEquals(expected, result);
    }

    @Test
    void updateTrigger() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        TriggerRequest request = TriggerRequestFixture.aDefaultTriggerRequest();
        Game game = GameFixture.aDefaultGame();
        Trigger trigger = TriggerFixture.aDefaultTrigger();
        TriggerResponse expected = TriggerResponseFixture.aDefaultTriggerResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(triggerService.updateTrigger(request, id, game)).thenReturn(trigger);
        when(triggerMapper.toResponse(trigger)).thenReturn(expected);

        TriggerResponse result = triggerController.updateTrigger(gameId, id, request);

        verify(triggerValidator).validate(request);
        verify(gameService).getById(gameId);
        verify(triggerService).updateTrigger(request, id, game);
        verify(triggerMapper).toResponse(trigger);

        assertEquals(expected, result);
    }

    @Test
    void deleteTrigger() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(gameService.getById(gameId)).thenReturn(game);

        triggerController.deleteTrigger(gameId, id);

        verify(gameService).getById(gameId);
        verify(triggerService).deleteByIdAndGame(id, game);
    }
}
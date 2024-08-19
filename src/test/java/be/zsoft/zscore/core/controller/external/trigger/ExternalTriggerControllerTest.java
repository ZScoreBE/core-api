package be.zsoft.zscore.core.controller.external.trigger;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerResponseFixture;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.trigger.TriggerService;
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
class ExternalTriggerControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;

    @Mock
    private TriggerService triggerService;

    @Mock
    private TriggerMapper triggerMapper;

    @InjectMocks
    private ExternalTriggerController externalTriggerController;

    @Test
    void getTriggers() {
        Pageable pageable = PageRequest.of(1, 10);
        Game game = GameFixture.aDefaultGame();
        Page<Trigger> triggers = new PageImpl<>(List.of(
                TriggerFixture.aDefaultTrigger(), TriggerFixture.aDefaultTrigger()
        ));
        List<TriggerResponse> expected = List.of(
                TriggerResponseFixture.aDefaultTriggerResponse(), TriggerResponseFixture.aDefaultTriggerResponse()
        );

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(triggerService.getTriggersByGame(game, pageable)).thenReturn(triggers);
        when(triggerMapper.toResponse(triggers)).thenReturn(new PageImpl<>(expected));

        PaginatedResponse<TriggerResponse> result = externalTriggerController.getTriggers(pageable);

        verify(gameService).getAuthenicatedGame();
        verify(triggerService).getTriggersByGame(game, pageable);
        verify(triggerMapper).toResponse(triggers);

        assertEquals(expected, result.items());
    }

    @Test
    void executeTrigger() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);

        externalTriggerController.executeTrigger(id);

        verify(playerService).getAuthenticatedPlayer();
        verify(triggerService).executeTrigger(id, player);
    }
}
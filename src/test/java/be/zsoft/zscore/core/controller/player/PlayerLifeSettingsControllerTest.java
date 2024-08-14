package be.zsoft.zscore.core.controller.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerLifeSettingsMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerLifeSettingsResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsRequestFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsResponseFixture;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerLifeSettingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerLifeSettingsControllerTest {

    @Mock
    private PlayerLifeSettingsMapper playerLifeSettingsMapper;

    @Mock
    private PlayerLifeSettingsService playerLifeSettingsService;

    @Mock
    private GameService gameService;

    @InjectMocks
    private PlayerLifeSettingsController playerLifeSettingsController;

    @Test
    void getPlayerLifeSettings() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        PlayerLifeSettingsResponse expected = PlayerLifeSettingsResponseFixture.aDefaultPlayerLifeSettingsResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerLifeSettingsService.getPlayerLifeSettings(game)).thenReturn(settings);
        when(playerLifeSettingsMapper.toResponse(settings)).thenReturn(expected);

        PlayerLifeSettingsResponse result = playerLifeSettingsController.getPlayerLifeSettings(gameId);

        verify(gameService).getById(gameId);
        verify(playerLifeSettingsService).getPlayerLifeSettings(game);
        verify(playerLifeSettingsMapper).toResponse(settings);

        assertEquals(expected, result);
    }

    @Test
    void updatePlayerLifeSettings() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        PlayerLifeSettingsRequest request = PlayerLifeSettingsRequestFixture.aDefaultPlayerLifeSettingsRequest();
        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        PlayerLifeSettingsResponse expected = PlayerLifeSettingsResponseFixture.aDefaultPlayerLifeSettingsResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerLifeSettingsService.updatePlayerLifeSettings(request, game)).thenReturn(settings);
        when(playerLifeSettingsMapper.toResponse(settings)).thenReturn(expected);

        PlayerLifeSettingsResponse result = playerLifeSettingsController.updatePlayerLifeSettings(gameId, request);

        verify(gameService).getById(gameId);
        verify(playerLifeSettingsService).updatePlayerLifeSettings(request, game);
        verify(playerLifeSettingsMapper).toResponse(settings);

        assertEquals(expected, result);
    }
}
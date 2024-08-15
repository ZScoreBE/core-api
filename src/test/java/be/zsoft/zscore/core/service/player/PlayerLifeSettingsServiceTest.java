package be.zsoft.zscore.core.service.player;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.player.PlayerLifeSettingsMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsRequestFixture;
import be.zsoft.zscore.core.repository.player.PlayerLifeSettingsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerLifeSettingsServiceTest {

    @Mock
    private PlayerLifeSettingsRepo playerLifeSettingsRepo;

    @Mock
    private PlayerLifeSettingsMapper playerLifeSettingsMapper;

    @InjectMocks
    private PlayerLifeSettingsService playerLifeSettingsService;

    @Captor
    private ArgumentCaptor<PlayerLifeSettings> playerLifeSettingsArgumentCaptor;

    @Test
    void getPlayerLifeSettings_success() {
        Game game = GameFixture.aDefaultGame();
        PlayerLifeSettings expected = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();

        when(playerLifeSettingsRepo.findByGame(game)).thenReturn(Optional.of(expected));

        PlayerLifeSettings result = playerLifeSettingsService.getPlayerLifeSettings(game);

        verify(playerLifeSettingsRepo).findByGame(game);

        assertEquals(expected, result);
    }

    @Test
    void getPlayerLifeSettings_notFound() {
        Game game = GameFixture.aDefaultGame();

        when(playerLifeSettingsRepo.findByGame(game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> playerLifeSettingsService.getPlayerLifeSettings(game));

        verify(playerLifeSettingsRepo).findByGame(game);
    }

    @Test
    void updatePlayerLifeSettings() {
        Game game = GameFixture.aDefaultGame();
        PlayerLifeSettingsRequest request = PlayerLifeSettingsRequestFixture.aDefaultPlayerLifeSettingsRequest();
        PlayerLifeSettings beforeUpdate = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        PlayerLifeSettings expected = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();

        when(playerLifeSettingsRepo.findByGame(game)).thenReturn(Optional.of(beforeUpdate));
        when(playerLifeSettingsMapper.fromRequest(request, beforeUpdate)).thenReturn(expected);
        when(playerLifeSettingsRepo.saveAndFlush(expected)).thenReturn(expected);

        PlayerLifeSettings result = playerLifeSettingsService.updatePlayerLifeSettings(request, game);

        verify(playerLifeSettingsRepo).findByGame(game);
        verify(playerLifeSettingsMapper).fromRequest(request, beforeUpdate);
        verify(playerLifeSettingsRepo).saveAndFlush(playerLifeSettingsArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(game, playerLifeSettingsArgumentCaptor.getValue().getGame());
    }

    @Test
    void getPlayerLifeSettingsAsOptional() {
        Game game = GameFixture.aDefaultGame();
        Optional<PlayerLifeSettings> expected = Optional.of(PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings());

        when(playerLifeSettingsRepo.findByGame(game)).thenReturn(expected);

        Optional<PlayerLifeSettings> result = playerLifeSettingsService.getPlayerLifeSettingsAsOptional(game);

        verify(playerLifeSettingsRepo).findByGame(game);

        assertEquals(expected, result);
    }
}
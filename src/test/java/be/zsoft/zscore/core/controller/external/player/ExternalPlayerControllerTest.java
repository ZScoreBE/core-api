package be.zsoft.zscore.core.controller.external.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerRequestFixtures;
import be.zsoft.zscore.core.fixtures.player.PlayerResponseFixture;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private ExternalPlayerController externalPlayerController;

    @Test
    void createPlayer() {
        PlayerRequest request = PlayerRequestFixtures.aDefaultPlayerRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        List<Currency> currencies = List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        );
        PlayerResponse expected = PlayerResponseFixture.aDefaultPlayerResponse();

        when(playerService.createPlayer(request)).thenReturn(player);
        when(currencyService.getAllCurrenciesByAuthenticatedGame()).thenReturn(currencies);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.createPlayer(request);

        assertEquals(expected, result);

        verify(playerService).createPlayer(request);
        verify(achievementProgressService).createAchievementProgressesForNewPlayer(player);
        verify(currencyService).getAllCurrenciesByAuthenticatedGame();
        verify(walletService).createWallets(currencies, player);
        verify(playerMapper).toResponse(player);
    }

    @Test
    void getMyself() {
        Player player = PlayerFixture.aDefaultPlayer();
        PlayerResponse expected = PlayerResponseFixture.aDefaultPlayerResponse();

        when(playerService.updateAuthenticatedPlayerLivesOnCount()).thenReturn(player);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.getMyself();

        verify(playerService).updateAuthenticatedPlayerLivesOnCount();
        verify(playerMapper).toResponse(player);

        assertEquals(expected, result);
    }

    @Test
    void takeLives() {
        Player player = PlayerFixture.aDefaultPlayer();
        PlayerResponse expected = PlayerResponseFixture.aDefaultPlayerResponse();

        when(playerService.takeLives(1)).thenReturn(player);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.takeLife(1);

        verify(playerService).takeLives(1);
        verify(playerMapper).toResponse(player);

        assertEquals(expected, result);
    }

    @Test
    void giveLives() {
        Player player = PlayerFixture.aDefaultPlayer();
        PlayerResponse expected = PlayerResponseFixture.aDefaultPlayerResponse();

        when(playerService.giveLives(1)).thenReturn(player);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.giveLife(1);

        verify(playerService).giveLives(1);
        verify(playerMapper).toResponse(player);

        assertEquals(expected, result);
    }
}
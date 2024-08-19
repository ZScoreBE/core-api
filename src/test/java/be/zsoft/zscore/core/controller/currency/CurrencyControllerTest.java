package be.zsoft.zscore.core.controller.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyRequestFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyResponseFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletService;
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
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void createCurrency() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        CurrencyRequest request = CurrencyRequestFixture.aDefaultCurrencyRequest();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        List<Player> players = List.of(PlayerFixture.aDefaultPlayer(), PlayerFixture.aDefaultPlayer());
        CurrencyResponse expected = CurrencyResponseFixture.aDefaultCurrencyResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.createCurrency(game, request)).thenReturn(currency);
        when(playerService.getAllPlayersByGame(game)).thenReturn(players);
        when(currencyMapper.toResponse(currency)).thenReturn(expected);

        CurrencyResponse response = currencyController.createCurrency(gameId, request);

        verify(gameService).getById(gameId);
        verify(currencyService).createCurrency(game, request);
        verify(playerService).getAllPlayersByGame(game);
        verify(walletService).createWallets(currency, players);
        verify(currencyMapper).toResponse(currency);

        assertEquals(expected, response);
    }

    @Test
    void getAllCurrencies() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        List<Currency> currencies = List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        );
        List<CurrencyResponse> expected = List.of(
                CurrencyResponseFixture.aDefaultCurrencyResponse(),
                CurrencyResponseFixture.aDefaultCurrencyResponse()
        );

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getAllCurrenciesByGame(game)).thenReturn(currencies);
        when(currencyMapper.toResponse(currencies)).thenReturn(expected);

        List<CurrencyResponse> result = currencyController.getAllCurrencies(gameId);

        verify(gameService).getById(gameId);
        verify(currencyService).getAllCurrenciesByGame(game);
        verify(currencyMapper).toResponse(currencies);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencies() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Page<Currency> currencies = new PageImpl<>(List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        ));
        Page<CurrencyResponse> expected = new PageImpl<>(List.of(
                CurrencyResponseFixture.aDefaultCurrencyResponse(),
                CurrencyResponseFixture.aDefaultCurrencyResponse()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrenciesByGame(game, pageable)).thenReturn(currencies);
        when(currencyMapper.toResponse(currencies)).thenReturn(expected);

        PaginatedResponse<CurrencyResponse> response = currencyController.getCurrencies(gameId, null, pageable);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrenciesByGame(game, pageable);
        verify(currencyMapper).toResponse(currencies);

        assertEquals(expected.getContent(), response.items());
    }

    @Test
    void searchCurrencies() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Page<Currency> currencies = new PageImpl<>(List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        ));
        Page<CurrencyResponse> expected = new PageImpl<>(List.of(
                CurrencyResponseFixture.aDefaultCurrencyResponse(),
                CurrencyResponseFixture.aDefaultCurrencyResponse()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.searchCurrenciesByGame("search", game, pageable)).thenReturn(currencies);
        when(currencyMapper.toResponse(currencies)).thenReturn(expected);

        PaginatedResponse<CurrencyResponse> response = currencyController.getCurrencies(gameId, "search", pageable);

        verify(gameService).getById(gameId);
        verify(currencyService).searchCurrenciesByGame("search", game, pageable);
        verify(currencyMapper).toResponse(currencies);

        assertEquals(expected.getContent(), response.items());
    }

    @Test
    void getCurrency() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse expected = CurrencyResponseFixture.aDefaultCurrencyResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, id)).thenReturn(currency);
        when(currencyMapper.toResponse(currency)).thenReturn(expected);

        CurrencyResponse response = currencyController.getCurrency(gameId, id);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, id);
        verify(currencyMapper).toResponse(currency);

        assertEquals(expected, response);
    }

    @Test
    void updateCurrency() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        CurrencyRequest request = CurrencyRequestFixture.aDefaultCurrencyRequest();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse expected = CurrencyResponseFixture.aDefaultCurrencyResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.updateCurrencyById(game, id, request)).thenReturn(currency);
        when(currencyMapper.toResponse(currency)).thenReturn(expected);

        CurrencyResponse response = currencyController.updateCurrency(gameId, id, request);

        verify(gameService).getById(gameId);
        verify(currencyService).updateCurrencyById(game, id, request);
        verify(currencyMapper).toResponse(currency);

        assertEquals(expected, response);
    }

    @Test
    void deleteCurrency() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(gameService.getById(gameId)).thenReturn(game);

        currencyController.deleteCurrency(gameId, id);

        verify(gameService).getById(gameId);
        verify(currencyService).deleteCurrencyById(game, id);

    }
}
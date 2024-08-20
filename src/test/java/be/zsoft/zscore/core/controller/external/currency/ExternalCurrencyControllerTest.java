package be.zsoft.zscore.core.controller.external.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferResponseFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyResponseFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.service.currency.CurrencyOfferService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
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
class ExternalCurrencyControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private CurrencyOfferService currencyOfferService;

    @Mock
    private CurrencyOfferMapper currencyOfferMapper;

    @InjectMocks
    private ExternalCurrencyController externalCurrencyController;

    @Test
    void getCurrencies() {
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

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(currencyService.getCurrenciesByGame(game, pageable)).thenReturn(currencies);
        when(currencyMapper.toResponse(currencies)).thenReturn(expected);

        PaginatedResponse<CurrencyResponse> response = externalCurrencyController.getCurrencies(pageable);

        verify(gameService).getAuthenicatedGame();
        verify(currencyService).getCurrenciesByGame(game, pageable);
        verify(currencyMapper).toResponse(currencies);

        assertEquals(expected.getContent(), response.items());
    }

    @Test
    void getCurrencyOffers() {
        UUID currencyId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Page<CurrencyOffer> currencyOffers = new PageImpl<>(List.of(
                CurrencyOfferFixture.aDefaultCurrencyOffer(),
                CurrencyOfferFixture.aDefaultCurrencyOffer()
        ));
        Page<CurrencyOfferResponse> expected = new PageImpl<>(List.of(
                CurrencyOfferResponseFixture.aDefaultCurrencyOfferRequest(),
                CurrencyOfferResponseFixture.aDefaultCurrencyOfferRequest()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.getCurrencyOffersByCurrency(currency, pageable)).thenReturn(currencyOffers);
        when(currencyOfferMapper.toResponse(currencyOffers)).thenReturn(expected);

        PaginatedResponse<CurrencyOfferResponse> result = externalCurrencyController.getCurrencyOffers(currencyId, pageable);

        verify(gameService).getAuthenicatedGame();
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).getCurrencyOffersByCurrency(currency, pageable);
        verify(currencyOfferMapper).toResponse(currencyOffers);

        assertEquals(expected.getContent(), result.items());
    }
}
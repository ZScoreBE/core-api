package be.zsoft.zscore.core.controller.currency;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferRequestFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferResponseFixture;
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
class CurrencyOfferControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyOfferMapper currencyOfferMapper;

    @Mock
    private CurrencyOfferService currencyOfferService;

    @InjectMocks
    private CurrencyOfferController currencyOfferController;

    @Test
    void createCurrencyOffer() {
        UUID gameId = UUID.randomUUID();
        UUID currencyId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer currencyOffer = CurrencyOfferFixture.aDefaultCurrencyOffer();
        CurrencyOfferRequest request = CurrencyOfferRequestFixture.aDefaultCurrencyOfferRequest();
        CurrencyOfferResponse expected = CurrencyOfferResponseFixture.aDefaultCurrencyOfferRequest();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.createCurrencyOffer(currency, request)).thenReturn(currencyOffer);
        when(currencyOfferMapper.toResponse(currencyOffer)).thenReturn(expected);

        CurrencyOfferResponse result = currencyOfferController.createCurrencyOffer(gameId, currencyId, request);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).createCurrencyOffer(currency, request);
        verify(currencyOfferMapper).toResponse(currencyOffer);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyOffers() {
        UUID gameId = UUID.randomUUID();
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

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.getCurrencyOffersByCurrency(currency, pageable)).thenReturn(currencyOffers);
        when(currencyOfferMapper.toResponse(currencyOffers)).thenReturn(expected);

        PaginatedResponse<CurrencyOfferResponse> result = currencyOfferController.getCurrencyOffers(gameId, currencyId, null, pageable);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).getCurrencyOffersByCurrency(currency, pageable);
        verify(currencyOfferMapper).toResponse(currencyOffers);

        assertEquals(expected.getContent(), result.items());
    }

    @Test
    void searchCurrencyOffers() {
        UUID gameId = UUID.randomUUID();
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

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.searchCurrencyOffersByCurrency("search", currency, pageable)).thenReturn(currencyOffers);
        when(currencyOfferMapper.toResponse(currencyOffers)).thenReturn(expected);

        PaginatedResponse<CurrencyOfferResponse> result = currencyOfferController.getCurrencyOffers(gameId, currencyId, "search", pageable);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).searchCurrencyOffersByCurrency("search", currency, pageable);
        verify(currencyOfferMapper).toResponse(currencyOffers);

        assertEquals(expected.getContent(), result.items());
    }

    @Test
    void getCurrencyOffer() {
        UUID gameId = UUID.randomUUID();
        UUID currencyId = UUID.randomUUID();
        UUID currencyOfferId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer currencyOffer = CurrencyOfferFixture.aDefaultCurrencyOffer();
        CurrencyOfferResponse expected = CurrencyOfferResponseFixture.aDefaultCurrencyOfferRequest();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.getCurrencyOfferById(currency, currencyOfferId)).thenReturn(currencyOffer);
        when(currencyOfferMapper.toResponse(currencyOffer)).thenReturn(expected);

        CurrencyOfferResponse result = currencyOfferController.getCurrencyOffer(gameId, currencyId, currencyOfferId);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).getCurrencyOfferById(currency, currencyOfferId);
        verify(currencyOfferMapper).toResponse(currencyOffer);

        assertEquals(expected, result);
    }

    @Test
    void updateCurrencyOffer() {
        UUID gameId = UUID.randomUUID();
        UUID currencyId = UUID.randomUUID();
        UUID currencyOfferId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer currencyOffer = CurrencyOfferFixture.aDefaultCurrencyOffer();
        CurrencyOfferRequest request = CurrencyOfferRequestFixture.aDefaultCurrencyOfferRequest();
        CurrencyOfferResponse expected = CurrencyOfferResponseFixture.aDefaultCurrencyOfferRequest();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);
        when(currencyOfferService.updateCurrencyOfferById(currency, currencyOfferId, request)).thenReturn(currencyOffer);
        when(currencyOfferMapper.toResponse(currencyOffer)).thenReturn(expected);

        CurrencyOfferResponse result = currencyOfferController.updateCurrencyOffer(gameId, currencyId, currencyOfferId, request);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).updateCurrencyOfferById(currency, currencyOfferId, request);
        verify(currencyOfferMapper).toResponse(currencyOffer);

        assertEquals(expected, result);
    }

    @Test
    void deleteCurrencyOffer() {
        UUID gameId = UUID.randomUUID();
        UUID currencyId = UUID.randomUUID();
        UUID currencyOfferId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency currency = CurrencyFixture.aDefaultCurrency();

        when(gameService.getById(gameId)).thenReturn(game);
        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);

        currencyOfferController.deleteCurrencyOffer(gameId, currencyId, currencyOfferId);

        verify(gameService).getById(gameId);
        verify(currencyService).getCurrencyById(game, currencyId);
        verify(currencyOfferService).deleteCurrencyOfferById(currency, currencyOfferId);
    }
}
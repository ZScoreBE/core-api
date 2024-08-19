package be.zsoft.zscore.core.service.currency;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyRequestFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.repository.currency.CurrencyRepo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyMapper currencyMapper;

    @Mock
    private CurrencyRepo currencyRepo;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void createCurrency() {
        Game game = GameFixture.aDefaultGame();
        CurrencyRequest request = CurrencyRequestFixture.aDefaultCurrencyRequest();
        Currency expected = CurrencyFixture.aDefaultCurrency();

        when(currencyMapper.fromRequest(request)).thenReturn(expected);
        when(currencyRepo.saveAndFlush(expected)).thenReturn(expected);

        Currency result = currencyService.createCurrency(game, request);

        verify(currencyMapper).fromRequest(request);
        verify(currencyRepo).saveAndFlush(expected);

        assertEquals(expected, result);
    }

    @Test
    void getCurrenciesByGame() {
        Game game = GameFixture.aDefaultGame();
        Page<Currency> expected = new PageImpl<>(List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        ));
        Pageable pageable = PageRequest.of(1,10);

        when(currencyRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Currency> result = currencyService.getCurrenciesByGame(game, pageable);

        verify(currencyRepo).findAllByGame(game, pageable);

        assertEquals(expected, result);
    }

    @Test
    void searchCurrenciesByGame() {
        Game game = GameFixture.aDefaultGame();
        Page<Currency> expected = new PageImpl<>(List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        ));
        Pageable pageable = PageRequest.of(1,10);

        when(currencyRepo.searchAllOnNameOrKeyByGame("%search%", game, pageable)).thenReturn(expected);

        Page<Currency> result = currencyService.searchCurrenciesByGame("search", game, pageable);

        verify(currencyRepo).searchAllOnNameOrKeyByGame("%search%", game, pageable);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyById_success() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency expected = CurrencyFixture.aDefaultCurrency();

        when(currencyRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));

        Currency result = currencyService.getCurrencyById(game, id);

        verify(currencyRepo).findByIdAndGame(id, game);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyById_notFound() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(currencyRepo.findByIdAndGame(id, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> currencyService.getCurrencyById(game, id));

        verify(currencyRepo).findByIdAndGame(id, game);
    }

    @Test
    void updateCurrencyById() {
        CurrencyRequest request = CurrencyRequestFixture.aDefaultCurrencyRequest();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency expected = CurrencyFixture.aDefaultCurrency();

        when(currencyRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));
        when(currencyMapper.fromRequest(request, expected)).thenReturn(expected);
        when(currencyRepo.saveAndFlush(expected)).thenReturn(expected);

        Currency result = currencyService.updateCurrencyById(game, id, request);

        verify(currencyRepo).findByIdAndGame(id, game);
        verify(currencyMapper).fromRequest(request, expected);
        verify(currencyRepo).saveAndFlush(expected);

        assertEquals(result, expected);
    }

    @Test
    void deleteCurrencyById() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Currency expected = CurrencyFixture.aDefaultCurrency();

        when(currencyRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));

        currencyService.deleteCurrencyById(game, id);

        verify(currencyRepo).delete(expected);
    }

    @Test
    void getAllCurrenciesByGame() {
        Game game = GameFixture.aDefaultGame();
        List<Currency> expected = List.of(
                CurrencyFixture.aDefaultCurrency(),
                CurrencyFixture.aDefaultCurrency()
        );

        when(currencyRepo.findAllByGame(game)).thenReturn(expected);

        List<Currency> result = currencyService.getAllCurrenciesByGame(game);

        verify(currencyRepo).findAllByGame(game);

        assertEquals(expected, result);
    }
}
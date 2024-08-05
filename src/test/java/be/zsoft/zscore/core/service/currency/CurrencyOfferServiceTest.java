package be.zsoft.zscore.core.service.currency;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyOfferRequestFixture;
import be.zsoft.zscore.core.repository.currency.CurrencyOfferRepo;
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
class CurrencyOfferServiceTest {

    @Mock
    private CurrencyOfferMapper currencyOfferMapper;

    @Mock
    private CurrencyOfferRepo currencyOfferRepo;

    @InjectMocks
    private CurrencyOfferService currencyOfferService;

    @Test
    void createCurrencyOffer() {
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOfferRequest request = CurrencyOfferRequestFixture.aDefaultCurrencyOfferRequest();
        CurrencyOffer expected = CurrencyOfferFixture.aDefaultCurrencyOffer();

        when(currencyOfferMapper.fromRequest(request)).thenReturn(expected);
        when(currencyOfferRepo.saveAndFlush(expected)).thenReturn(expected);

        CurrencyOffer result = currencyOfferService.createCurrencyOffer(currency, request);

        verify(currencyOfferMapper).fromRequest(request);
        verify(currencyOfferRepo).saveAndFlush(expected);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyOffersByCurrency() {
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Page<CurrencyOffer> expected = new PageImpl<>(List.of(
                CurrencyOfferFixture.aDefaultCurrencyOffer(),
                CurrencyOfferFixture.aDefaultCurrencyOffer()
        ));
        Pageable pageable = PageRequest.of(1,10);

        when(currencyOfferRepo.findAllByCurrency(currency, pageable)).thenReturn(expected);

        Page<CurrencyOffer> result = currencyOfferRepo.findAllByCurrency(currency, pageable);

        verify(currencyOfferRepo).findAllByCurrency(currency, pageable);

        assertEquals(expected, result);
    }

    @Test
    void searchCurrencyOffersByCurrency() {
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Page<CurrencyOffer> expected = new PageImpl<>(List.of(
                CurrencyOfferFixture.aDefaultCurrencyOffer(),
                CurrencyOfferFixture.aDefaultCurrencyOffer()
        ));
        Pageable pageable = PageRequest.of(1,10);

        when(currencyOfferRepo.searchAllByCurrency("%search%", currency, pageable)).thenReturn(expected);

        Page<CurrencyOffer> result = currencyOfferService.searchCurrencyOffersByCurrency("search", currency, pageable);

        verify(currencyOfferRepo).searchAllByCurrency("%search%", currency, pageable);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyOfferById_success() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer expected = CurrencyOfferFixture.aDefaultCurrencyOffer();

        when(currencyOfferRepo.findByIdAndCurrency(id, currency)).thenReturn(Optional.of(expected));

        CurrencyOffer result = currencyOfferService.getCurrencyOfferById(currency, id);

        verify(currencyOfferRepo).findByIdAndCurrency(id, currency);

        assertEquals(expected, result);
    }

    @Test
    void getCurrencyOfferById_notFound() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();

        when(currencyOfferRepo.findByIdAndCurrency(id, currency)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> currencyOfferService.getCurrencyOfferById(currency, id));

        verify(currencyOfferRepo).findByIdAndCurrency(id, currency);
    }

    @Test
    void updateCurrencyOfferById() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer expected = CurrencyOfferFixture.aDefaultCurrencyOffer();
        CurrencyOfferRequest request = CurrencyOfferRequestFixture.aDefaultCurrencyOfferRequest();

        when(currencyOfferRepo.findByIdAndCurrency(id, currency)).thenReturn(Optional.of(expected));
        when(currencyOfferMapper.fromRequest(request, expected)).thenReturn(expected);
        when(currencyOfferRepo.saveAndFlush(expected)).thenReturn(expected);

        CurrencyOffer result = currencyOfferService.updateCurrencyOfferById(currency, id, request);

        verify(currencyOfferRepo).findByIdAndCurrency(id, currency);
    }

    @Test
    void deleteCurrencyOfferById() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyOffer expected = CurrencyOfferFixture.aDefaultCurrencyOffer();

        when(currencyOfferRepo.findByIdAndCurrency(id, currency)).thenReturn(Optional.of(expected));

        currencyOfferService.deleteCurrencyOfferById(currency, id);

        verify(currencyOfferRepo).delete(expected);
    }
}
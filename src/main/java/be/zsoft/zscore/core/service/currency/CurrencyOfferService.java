package be.zsoft.zscore.core.service.currency;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.currency.CurrencyOfferMapper;
import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import be.zsoft.zscore.core.repository.currency.CurrencyOfferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CurrencyOfferService {

    private final CurrencyOfferMapper currencyOfferMapper;
    private final CurrencyOfferRepo currencyOfferRepo;

    public CurrencyOffer createCurrencyOffer(Currency currency, CurrencyOfferRequest request) {
        CurrencyOffer offer = currencyOfferMapper.fromRequest(request);
        offer.setCurrency(currency);

        return currencyOfferRepo.saveAndFlush(offer);
    }

    public Page<CurrencyOffer> getCurrencyOffersByCurrency(Currency currency, Pageable pageable) {
        return currencyOfferRepo.findAllByCurrency(currency, pageable);
    }

    public Page<CurrencyOffer> searchCurrencyOffersByCurrency(String search, Currency currency, Pageable pageable) {
        return currencyOfferRepo.searchAllByCurrency("%" + search.toLowerCase() + "%", currency, pageable);
    }

    public CurrencyOffer getCurrencyOfferById(Currency currency, UUID id) {
        return currencyOfferRepo.findByIdAndCurrency(id, currency)
                .orElseThrow(() -> new NotFoundException("Could not find currency offer '%s' from currency '%s'".formatted(id, currency.getId())));
    }

    public CurrencyOffer updateCurrencyOfferById(Currency currency, UUID id, CurrencyOfferRequest request) {
        CurrencyOffer offer = currencyOfferMapper.fromRequest(request, getCurrencyOfferById(currency, id));

        return currencyOfferRepo.saveAndFlush(offer);
    }

    public void deleteCurrencyOfferById(Currency currency, UUID id) {
        CurrencyOffer offer = getCurrencyOfferById(currency, id);
        currencyOfferRepo.delete(offer);
    }
}


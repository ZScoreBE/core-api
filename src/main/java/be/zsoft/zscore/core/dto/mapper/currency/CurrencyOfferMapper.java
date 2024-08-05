package be.zsoft.zscore.core.dto.mapper.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
public class CurrencyOfferMapper {

    public CurrencyOffer fromRequest(CurrencyOfferRequest request) {
        return fromRequest(request, new CurrencyOffer());
    }

    public CurrencyOffer fromRequest(CurrencyOfferRequest request, CurrencyOffer currencyOffer) {
        currencyOffer.setName(request.name());
        currencyOffer.setKey(request.key());
        currencyOffer.setAmount(request.amount());
        currencyOffer.setPriceEx(request.priceEx().setScale(2, RoundingMode.HALF_UP));
        currencyOffer.setDisCountPriceEx(request.discountPriceEx() != null ? request.discountPriceEx().setScale(2, RoundingMode.HALF_UP) : null);

        return currencyOffer;
    }

    public CurrencyOfferResponse toResponse(CurrencyOffer currencyOffer) {
        return new CurrencyOfferResponse(
                currencyOffer.getId(),
                currencyOffer.getName(),
                currencyOffer.getKey(),
                currencyOffer.getAmount(),
                currencyOffer.getPriceEx().setScale(2, RoundingMode.HALF_UP),
                currencyOffer.getDisCountPriceEx() != null ? currencyOffer.getDisCountPriceEx().setScale(2, RoundingMode.HALF_UP) : null
        );
    }

    public Page<CurrencyOfferResponse> toResponse(Page<CurrencyOffer> currencyOffers) {
        return currencyOffers.map(this::toResponse);
    }
}

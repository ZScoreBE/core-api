package be.zsoft.zscore.core.dto.mapper.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyMapper {

    public Currency fromRequest(CurrencyRequest request) {
        return fromRequest(request, new Currency());
    }

    public Currency fromRequest(CurrencyRequest request, Currency currency) {
        currency.setName(request.name());
        currency.setKey(request.key());

        return currency;
    }

    public CurrencyResponse toResponse(Currency currency) {
        return new CurrencyResponse(
                currency.getId(),
                currency.getName(),
                currency.getKey()
        );
    }

    public Page<CurrencyResponse> toResponse(Page<Currency> currencies) {
        return currencies.map(this::toResponse);
    }

    public List<CurrencyResponse> toResponse(List<Currency> currencies) {
        return currencies.stream().map(this::toResponse).toList();
    }
}

package be.zsoft.zscore.core.dto.mapper.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrencyMapperTest {

    @InjectMocks
    private CurrencyMapper currencyMapper;

    @Test
    void fromRequest_create() {
        CurrencyRequest request = new CurrencyRequest("GOLD", "GLD");
        Currency expected = Currency.builder()
                .name("GOLD")
                .key("GLD")
                .build();

        Currency result = currencyMapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void fromRequest_update() {
        UUID currencyId = UUID.randomUUID();
        CurrencyRequest request = new CurrencyRequest("GOLD", "GLD");
        Currency existing = Currency.builder().id(currencyId).build();
        Currency expected = Currency.builder()
                .id(currencyId)
                .name("GOLD")
                .key("GLD")
                .build();

        Currency result = currencyMapper.fromRequest(request, existing);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Currency currency = Currency.builder()
                .id(id)
                .name("GOLD")
                .key("GLD")
                .build();
        CurrencyResponse expected = new CurrencyResponse(id, "GOLD", "GLD");

        CurrencyResponse result = currencyMapper.toResponse(currency);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_page() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Currency currency1 = Currency.builder()
                .id(id1)
                .name("GOLD")
                .key("GLD")
                .build();
        Currency currency2 = Currency.builder()
                .id(id2)
                .name("EMERALD")
                .key("EMR")
                .build();
        Page<Currency> currencies = new PageImpl<>(List.of(currency1, currency2));
        Page<CurrencyResponse> expected = new PageImpl<>(List.of(
                new CurrencyResponse(id1, "GOLD", "GLD"),
                new CurrencyResponse(id2, "EMERALD", "EMR")
        ));

        Page<CurrencyResponse> result = currencyMapper.toResponse(currencies);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_list() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Currency currency1 = Currency.builder()
                .id(id1)
                .name("GOLD")
                .key("GLD")
                .build();
        Currency currency2 = Currency.builder()
                .id(id2)
                .name("EMERALD")
                .key("EMR")
                .build();
        List<Currency> currencies = List.of(currency1, currency2);
        List<CurrencyResponse> expected = List.of(
                new CurrencyResponse(id1, "GOLD", "GLD"),
                new CurrencyResponse(id2, "EMERALD", "EMR")
        );

        List<CurrencyResponse> result = currencyMapper.toResponse(currencies);

        assertEquals(expected, result);
    }
}
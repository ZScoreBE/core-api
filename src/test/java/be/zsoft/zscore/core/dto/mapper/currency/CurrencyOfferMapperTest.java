package be.zsoft.zscore.core.dto.mapper.currency;

import be.zsoft.zscore.core.dto.request.currency.CurrencyOfferRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyOfferResponse;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CurrencyOfferMapperTest {

    @InjectMocks
    private CurrencyOfferMapper currencyOfferMapper;

    @Test
    void fromRequest_create() {
        CurrencyOfferRequest request = new CurrencyOfferRequest("GOLD", "GLD", 1000, new BigDecimal("10.00"), new BigDecimal("5.50"));
        CurrencyOffer expected = CurrencyOffer.builder()
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();

        CurrencyOffer result = currencyOfferMapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void fromRequest_update() {
        UUID currencyOfferId = UUID.randomUUID();
        CurrencyOfferRequest request = new CurrencyOfferRequest("GOLD", "GLD", 1000, new BigDecimal("10.00"), new BigDecimal("5.50"));
        CurrencyOffer existing = CurrencyOffer.builder().id(currencyOfferId).build();
        CurrencyOffer expected = CurrencyOffer.builder()
                .id(currencyOfferId)
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();

        CurrencyOffer result = currencyOfferMapper.fromRequest(request, existing);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        CurrencyOffer currencyOffer = CurrencyOffer.builder()
                .id(id)
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();
        CurrencyOfferResponse expected = new CurrencyOfferResponse(id, "GOLD", "GLD", 1000, new BigDecimal("10.00"), new BigDecimal("5.50"));

        CurrencyOfferResponse result = currencyOfferMapper.toResponse(currencyOffer);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        CurrencyOffer currencyOffer1 = CurrencyOffer.builder()
                .id(id1)
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();
        CurrencyOffer currencyOffer2 = CurrencyOffer.builder()
                .id(id2)
                .name("GOLD")
                .key("GLD")
                .amount(1000)
                .priceEx(new BigDecimal("10.00"))
                .disCountPriceEx(new BigDecimal("5.50"))
                .build();
        Page<CurrencyOffer> offers = new PageImpl<>(List.of(currencyOffer1, currencyOffer2));
        Page<CurrencyOfferResponse> expected = new PageImpl<>(List.of(
                new CurrencyOfferResponse(id1, "GOLD", "GLD", 1000, new BigDecimal("10.00"), new BigDecimal("5.50")),
                new CurrencyOfferResponse(id2, "GOLD", "GLD", 1000, new BigDecimal("10.00"), new BigDecimal("5.50"))
        ));

        Page<CurrencyOfferResponse> result = currencyOfferMapper.toResponse(offers);

        assertEquals(expected, result);
    }
}
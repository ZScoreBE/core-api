package be.zsoft.zscore.core.dto.mapper.trigger;

import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyResponseFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriggerMapperTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private TriggerMapper triggerMapper;

    @Test
    void fromRequest_create() {
        UUID currencyId = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Game game = GameFixture.aDefaultGame();

        TriggerRequest request = new TriggerRequest(
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                10,
                null,
                TriggerRewardType.CURRENCY,
                null,
                currencyId
        );
        Trigger expected = Trigger.builder()
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costAmount(10)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardCurrency(currency)
                .build();

        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);

        Trigger result = triggerMapper.fromRequest(request, game);

        verify(currencyService).getCurrencyById(game, currencyId);

        assertEquals(expected, result);
    }

    @Test
    void fromRequest_update() {
        UUID id = UUID.randomUUID();
        UUID currencyId = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Game game = GameFixture.aDefaultGame();

        TriggerRequest request = new TriggerRequest(
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                10,
                null,
                TriggerRewardType.CURRENCY,
                null,
                currencyId
        );
        Trigger existing = Trigger.builder().id(id).build();
        Trigger expected = Trigger.builder()
                .id(id)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costAmount(10)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardCurrency(currency)
                .build();

        when(currencyService.getCurrencyById(game, currencyId)).thenReturn(currency);

        Trigger result = triggerMapper.fromRequest(request, game, existing);

        assertEquals(expected, result);
    }


    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse currencyResponse = CurrencyResponseFixture.aDefaultCurrencyResponse();

        Trigger trigger = Trigger.builder()
                .id(id)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costAmount(10)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardCurrency(currency)
                .build();
        TriggerResponse expected = new TriggerResponse(
                id,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                10,
                null,
                null,
                currencyResponse
        );

        when(currencyMapper.toResponse(currency)).thenReturn(currencyResponse);

        TriggerResponse result = triggerMapper.toResponse(trigger);

        verify(currencyMapper).toResponse(currency);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse currencyResponse = CurrencyResponseFixture.aDefaultCurrencyResponse();
        Trigger trigger1 = Trigger.builder()
                .id(id1)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costAmount(10)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardCurrency(currency)
                .build();
        Trigger trigger2 = Trigger.builder()
                .id(id2)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costAmount(10)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardCurrency(currency)
                .build();
        TriggerResponse expected1 = new TriggerResponse(
                id1,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                10,
                null,
                null,
                currencyResponse
        );
        TriggerResponse expected2 = new TriggerResponse(
                id2,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                10,
                null,
                null,
                currencyResponse
        );

        Page<Trigger> triggers = new PageImpl<>(List.of(trigger1, trigger2));
        Page<TriggerResponse> expected = new PageImpl<>(List.of(expected1, expected2));

        when(currencyMapper.toResponse(currency)).thenReturn(currencyResponse);

        Page<TriggerResponse> result = triggerMapper.toResponse(triggers);

        verify(currencyMapper, times(2)).toResponse(currency);

        assertEquals(expected, result);
    }
}
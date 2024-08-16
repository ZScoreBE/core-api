package be.zsoft.zscore.core.dto.mapper.trigger;

import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TriggerMapperTest {

    @InjectMocks
    private TriggerMapper triggerMapper;

    @Test
    void fromRequest_create() {
        TriggerRequest request = new TriggerRequest(
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of("amount", "10"),
                Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220")
        );
        Trigger expected = Trigger.builder()
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costMetaData(Map.of("amount", "10"))
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardMetaData(Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220"))
                .build();

        Trigger result = triggerMapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void fromRequest_update() {
        UUID id = UUID.randomUUID();
        TriggerRequest request = new TriggerRequest(
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of("amount", "10"),
                Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220")
        );
        Trigger existing = Trigger.builder().id(id).build();
        Trigger expected = Trigger.builder()
                .id(id)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costMetaData(Map.of("amount", "10"))
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardMetaData(Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220"))
                .build();

        Trigger result = triggerMapper.fromRequest(request, existing);

        assertEquals(expected, result);
    }


    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Trigger trigger = Trigger.builder()
                .id(id)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costMetaData(Map.of("amount", "10"))
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardMetaData(Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220"))
                .build();
        TriggerResponse expected = new TriggerResponse(
                id,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of("amount", "10"),
                Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220")
        );

        TriggerResponse result = triggerMapper.toResponse(trigger);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Trigger trigger1 = Trigger.builder()
                .id(id1)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costMetaData(Map.of("amount", "10"))
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardMetaData(Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220"))
                .build();
        Trigger trigger2 = Trigger.builder()
                .id(id2)
                .name("Currency trigger")
                .key("CRNCY")
                .costType(TriggerCostType.FREE)
                .costMetaData(Map.of("amount", "10"))
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardMetaData(Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220"))
                .build();
        TriggerResponse expected1 = new TriggerResponse(
                id1,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of("amount", "10"),
                Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220")
        );
        TriggerResponse expected2 = new TriggerResponse(
                id2,
                "Currency trigger",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of("amount", "10"),
                Map.of("currencyId", "43a7f353-2a1e-4a3e-9510-da8b76704220")
        );

        Page<Trigger> triggers = new PageImpl<>(List.of(trigger1, trigger2));
        Page<TriggerResponse> expected = new PageImpl<>(List.of(expected1, expected2));

        Page<TriggerResponse> result = triggerMapper.toResponse(triggers);

        assertEquals(expected, result);
    }
}
package be.zsoft.zscore.core.fixtures.trigger;

import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class TriggerRequestFixture {

    public static TriggerRequest aDefaultTriggerRequest() {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.FREE,
                TriggerRewardType.LIVES,
                Map.of(),
                Map.of("amount", Integer.toString(10))
        );
    }

    public static TriggerRequest aCurrencyCostTriggerWithCostMetadata(Map<String, String> metadata) {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.CURRENCY,
                TriggerRewardType.LIVES,
                metadata,
                Map.of()
        );
    }

    public static TriggerRequest aLivesRewardWithRewardMetadata(Map<String, String> metadata) {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.FREE,
                TriggerRewardType.LIVES,
                Map.of(),
                metadata
        );
    }

    public static TriggerRequest aCurrencyRewardWithRewardMetadata(Map<String, String> metadata) {
        return new TriggerRequest(
                "Give currency",
                "CRNCY",
                TriggerCostType.FREE,
                TriggerRewardType.CURRENCY,
                Map.of(),
                metadata
        );
    }
}

package be.zsoft.zscore.core.fixtures.trigger;

import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TriggerRequestFixture {

    public static TriggerRequest aDefaultTriggerRequest() {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.FREE,
                null,
                null,
                TriggerRewardType.LIVES,
                10,
                null
        );
    }

    public static TriggerRequest aCurrencyCostTriggerWithCostMetadata(Integer amount, UUID currencyId) {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.CURRENCY,
                amount,
                currencyId,
                TriggerRewardType.LIVES,
                null,
                null
        );
    }

    public static TriggerRequest aLivesRewardWithRewardMetadata(Integer amount) {
        return new TriggerRequest(
                "Give Lives",
                "LVS",
                TriggerCostType.FREE,
                null,
                null,
                TriggerRewardType.LIVES,
                amount,
                null
        );
    }

    public static TriggerRequest aCurrencyRewardWithRewardMetadata(Integer amount, UUID currencyId) {
        return new TriggerRequest(
                "Give currency",
                "CRNCY",
                TriggerCostType.FREE,
                null,
                null,
                TriggerRewardType.CURRENCY,
                amount,
                currencyId
        );
    }
}

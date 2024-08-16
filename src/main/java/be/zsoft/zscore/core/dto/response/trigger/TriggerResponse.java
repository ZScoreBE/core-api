package be.zsoft.zscore.core.dto.response.trigger;

import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;

import java.util.Map;
import java.util.UUID;

public record TriggerResponse(
        UUID id,
        String name,
        String key,
        TriggerCostType costType,
        TriggerRewardType rewardType,
        Map<String, String> costMetaData,
        Map<String, String> rewardMetaData
) {
}

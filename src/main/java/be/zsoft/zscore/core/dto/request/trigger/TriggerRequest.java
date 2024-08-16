package be.zsoft.zscore.core.dto.request.trigger;

import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record TriggerRequest(
        @NotBlank String name,
        @NotBlank @Size(max = 5) String key,
        @NotNull TriggerCostType costType,
        @NotNull TriggerRewardType rewardType,
        @NotNull Map<String, String> costMetaData,
        @NotNull Map<String, String> rewardMetaData
) {
}

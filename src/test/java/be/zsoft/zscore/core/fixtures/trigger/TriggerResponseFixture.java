package be.zsoft.zscore.core.fixtures.trigger;

import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TriggerResponseFixture {

    public static TriggerResponse aDefaultTriggerResponse() {
        return new TriggerResponse(
                UUID.randomUUID(),
                "Give Lives",
                "LVS",
                TriggerCostType.FREE,
                TriggerRewardType.LIVES,
                null,
                null,
                10,
                null
        );
    }
}

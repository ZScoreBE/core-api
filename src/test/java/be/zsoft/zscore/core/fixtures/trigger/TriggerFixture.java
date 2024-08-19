package be.zsoft.zscore.core.fixtures.trigger;

import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import be.zsoft.zscore.core.entity.trigger.TriggerRewardType;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TriggerFixture {

    public static Trigger aDefaultTrigger() {
        return Trigger.builder()
                .id(UUID.randomUUID())
                .name("Give lives")
                .key("LVS")
                .costType(TriggerCostType.FREE)
                .rewardType(TriggerRewardType.LIVES)
                .rewardAmount(10)
                .build();
    }
}

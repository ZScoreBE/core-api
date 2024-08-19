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

    public static Trigger aCostFreeRewardCurrencyTrigger() {
        return Trigger.builder()
                .id(UUID.randomUUID())
                .name("Give currency")
                .key("GCY")
                .costType(TriggerCostType.FREE)
                .rewardType(TriggerRewardType.CURRENCY)
                .rewardAmount(100)
                .build();
    }

    public static Trigger aCostCurrencyRewardLivesTrigger() {
        return Trigger.builder()
                .id(UUID.randomUUID())
                .name("Reward lives")
                .key("RLVS")
                .costType(TriggerCostType.CURRENCY)
                .costAmount(100)
                .rewardType(TriggerRewardType.LIVES)
                .rewardAmount(100)
                .build();
    }
}

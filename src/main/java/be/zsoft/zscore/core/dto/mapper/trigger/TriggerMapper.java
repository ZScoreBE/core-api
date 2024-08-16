package be.zsoft.zscore.core.dto.mapper.trigger;

import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TriggerMapper {

    public Trigger fromRequest(TriggerRequest request) {
        return fromRequest(request, new Trigger());
    }

    public Trigger fromRequest(TriggerRequest request, Trigger trigger) {
        trigger.setName(request.name());
        trigger.setKey(request.key());
        trigger.setCostType(request.costType());
        trigger.setCostMetaData(request.costMetaData());
        trigger.setRewardType(request.rewardType());
        trigger.setRewardMetaData(request.rewardMetaData());

        return trigger;
    }

    public TriggerResponse toResponse(Trigger trigger) {
        return new TriggerResponse(
                trigger.getId(),
                trigger.getName(),
                trigger.getKey(),
                trigger.getCostType(),
                trigger.getRewardType(),
                trigger.getCostMetaData(),
                trigger.getRewardMetaData()
        );
    }

    public Page<TriggerResponse> toResponse(Page<Trigger> triggers) {
        return triggers.map(this::toResponse);
    }
}

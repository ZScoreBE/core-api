package be.zsoft.zscore.core.dto.mapper.trigger;

import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.dto.response.trigger.TriggerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TriggerMapper {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    public Trigger fromRequest(TriggerRequest request, Game game) {
        return fromRequest(request, game, new Trigger());
    }

    public Trigger fromRequest(TriggerRequest request, Game game, Trigger trigger) {
        trigger.setName(request.name());
        trigger.setKey(request.key());
        trigger.setCostType(request.costType());
        trigger.setCostAmount(request.costAmount());

        if (request.costCurrencyId() != null) {
            trigger.setCostCurrency(currencyService.getCurrencyById(game, request.costCurrencyId()));
        }

        trigger.setRewardType(request.rewardType());
        trigger.setRewardAmount(request.rewardAmount());

        if (request.rewardCurrencyId() != null) {
            trigger.setRewardCurrency(currencyService.getCurrencyById(game, request.rewardCurrencyId()));
        }

        return trigger;
    }

    public TriggerResponse toResponse(Trigger trigger) {
        return new TriggerResponse(
                trigger.getId(),
                trigger.getName(),
                trigger.getKey(),
                trigger.getCostType(),
                trigger.getRewardType(),
                trigger.getCostAmount(),
                trigger.getCostCurrency() != null ? currencyMapper.toResponse(trigger.getCostCurrency()) : null,
                trigger.getRewardAmount(),
                trigger.getRewardCurrency() != null ? currencyMapper.toResponse(trigger.getRewardCurrency()) : null
        );
    }

    public Page<TriggerResponse> toResponse(Page<Trigger> triggers) {
        return triggers.map(this::toResponse);
    }
}

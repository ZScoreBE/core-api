package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TriggerValidator {

    private static final String CURRENCY_ID_KEY = "currencyId";
    private static final String AMOUNT_KEY = "amount";

    public void validate(TriggerRequest request) {
        if (request.costType() == TriggerCostType.CURRENCY) {
            validateCurrencyCostMetadata(request);
        }

        switch (request.rewardType()) {
            case LIVES -> validateLivesRewardMetadata(request);
            case CURRENCY -> validateCurrencyRewardMetadata(request);
        }
    }

    private void validateCurrencyCostMetadata(TriggerRequest request) {
        if (request.costCurrencyId() == null) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_CURRENCY_ID_COST_META_DATA);
        }

        if (request.costAmount() == null) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_COST_META_DATA);
        }
    }

    private void validateLivesRewardMetadata(TriggerRequest request) {
        if (request.rewardAmount() == null) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA);
        }
    }

    private void validateCurrencyRewardMetadata(TriggerRequest request) {
        if (request.rewardCurrencyId() == null) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_CURRENCY_ID_REWARD_META_DATA);
        }

        if (request.rewardAmount() == null) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA);
        }
    }
}

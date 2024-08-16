package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.trigger.TriggerCostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class TriggerValidator {

    private static final String CURRENCY_ID_KEY = "currencyId";
    private static final String AMOUNT_KEY = "amount";

    public void validate(TriggerRequest request) {
        if (request.costType() == TriggerCostType.CURRENCY) {
            validateCurrencyCostMetadata(request.costMetaData());
        }

        switch (request.rewardType()) {
            case LIVES -> validateLivesRewardMetadata(request.rewardMetaData());
            case CURRENCY -> validateCurrencyRewardMetadata(request.rewardMetaData());
        }
    }

    private void validateCurrencyCostMetadata(Map<String, String> metaData) {
        if (!metaData.containsKey(CURRENCY_ID_KEY)) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_CURRENCY_ID_COST_META_DATA);
        }

        if (!metaData.containsKey(AMOUNT_KEY)) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_COST_META_DATA);
        }
    }

    private void validateLivesRewardMetadata(Map<String, String> metaData) {
        if (!metaData.containsKey(AMOUNT_KEY)) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA);
        }
    }

    private void validateCurrencyRewardMetadata(Map<String, String> metaData) {
        if (!metaData.containsKey(CURRENCY_ID_KEY)) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_CURRENCY_ID_REWARD_META_DATA);
        }

        if (!metaData.containsKey(AMOUNT_KEY)) {
            throw new ApiException(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA);
        }
    }
}

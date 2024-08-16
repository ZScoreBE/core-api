package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.fixtures.trigger.TriggerRequestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TriggerValidatorTest {

    @InjectMocks
    private TriggerValidator triggerValidator;

    @Test
    void validate_noCurrencyIdCostMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyCostTriggerWithCostMetadata(Map.of("amount", "10"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_CURRENCY_ID_COST_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_noAmountIdCostMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyCostTriggerWithCostMetadata(Map.of("currencyId", "08187622-405a-454f-867a-2f558e06de53"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_COST_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_lives_noAmountIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aLivesRewardWithRewardMetadata(Map.of());
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_currency_noCurrencyIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyRewardWithRewardMetadata(Map.of("amount", "10"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_CURRENCY_ID_REWARD_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_currency_noAmountIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyRewardWithRewardMetadata(Map.of("currencyId", "310ccf48-5567-4b52-bb6a-fcf89413f80e"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA, ex.getErrorKey());
    }
}
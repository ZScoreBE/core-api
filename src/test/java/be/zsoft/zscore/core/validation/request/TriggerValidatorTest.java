package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.fixtures.trigger.TriggerRequestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TriggerValidatorTest {

    @InjectMocks
    private TriggerValidator triggerValidator;

    @Test
    void validate_noCurrencyIdCostMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyCostTriggerWithCostMetadata(10, null);
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_CURRENCY_ID_COST_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_noAmountIdCostMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyCostTriggerWithCostMetadata(null, UUID.fromString("08187622-405a-454f-867a-2f558e06de53"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_COST_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_lives_noAmountIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aLivesRewardWithRewardMetadata(null);
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_currency_noCurrencyIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyRewardWithRewardMetadata(10, null);
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_CURRENCY_ID_REWARD_META_DATA, ex.getErrorKey());
    }

    @Test
    void validate_currency_noAmountIdRewardMetaData() {
        TriggerRequest request = TriggerRequestFixture.aCurrencyRewardWithRewardMetadata(null, UUID.fromString("310ccf48-5567-4b52-bb6a-fcf89413f80e"));
        ApiException ex = assertThrows(ApiException.class, () -> triggerValidator.validate(request));

        assertEquals(ErrorCodes.TRIGGER_NO_AMOUNT_REWARD_META_DATA, ex.getErrorKey());
    }
}
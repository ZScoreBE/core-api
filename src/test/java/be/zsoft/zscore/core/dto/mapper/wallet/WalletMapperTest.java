package be.zsoft.zscore.core.dto.mapper.wallet;

import be.zsoft.zscore.core.dto.mapper.currency.CurrencyMapper;
import be.zsoft.zscore.core.dto.response.currency.CurrencyResponse;
import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.currency.CurrencyResponseFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletMapperTest {

    @Mock
    private CurrencyMapper currencyMapper;

    @InjectMocks
    private WalletMapper walletMapper;

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse currencyResponse = CurrencyResponseFixture.aDefaultCurrencyResponse();
        Wallet wallet = Wallet.builder()
                .id(id)
                .amount(2000)
                .currency(currency)
                .build();
        WalletResponse expected = new WalletResponse(id, 2000, currencyResponse);

        when(currencyMapper.toResponse(currency)).thenReturn(currencyResponse);

        WalletResponse result = walletMapper.toResponse(wallet);

        verify(currencyMapper).toResponse(currency);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Currency currency1 = CurrencyFixture.aDefaultCurrency();
        Currency currency2 = CurrencyFixture.aDefaultCurrency();
        CurrencyResponse currencyResponse1 = CurrencyResponseFixture.aDefaultCurrencyResponse();
        CurrencyResponse currencyResponse2 = CurrencyResponseFixture.aDefaultCurrencyResponse();
        Wallet wallet1 = Wallet.builder()
                .id(id1)
                .amount(2000)
                .currency(currency1)
                .build();
        Wallet wallet2 = Wallet.builder()
                .id(id2)
                .amount(2000)
                .currency(currency2)
                .build();
        WalletResponse expected1 = new WalletResponse(id1, 2000, currencyResponse1);
        WalletResponse expected2 = new WalletResponse(id2, 2000, currencyResponse2);
        Page<Wallet> wallets = new PageImpl<>(List.of(wallet1, wallet2));
        Page<WalletResponse> expectedPage = new PageImpl<>(List.of(expected1, expected2));

        when(currencyMapper.toResponse(currency1)).thenReturn(currencyResponse1);
        when(currencyMapper.toResponse(currency2)).thenReturn(currencyResponse2);

        Page<WalletResponse> result = walletMapper.toResponse(wallets);

        verify(currencyMapper).toResponse(currency1);
        verify(currencyMapper).toResponse(currency2);

        assertEquals(expectedPage   , result);
    }
}
package be.zsoft.zscore.core.dto.mapper.wallet;

import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletOperationResponse;
import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletResponseFixture;
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
class WalletOperationMapperTest {

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletOperationMapper walletOperationMapper;

    @Test
    void fromRequest() {
        WalletOperationRequest request = new WalletOperationRequest(WalletOperationType.INCREASE, 2000);
        WalletOperation expected = WalletOperation.builder()
                .type(WalletOperationType.INCREASE)
                .amount(2000)
                .build();

        WalletOperation result = walletOperationMapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Wallet wallet = WalletFixture.aDefaultWallet();
        WalletResponse walletResponse = WalletResponseFixture.aDefaultWalletResponse();
        WalletOperation walletOperation = WalletOperation.builder()
                .id(id)
                .type(WalletOperationType.INCREASE)
                .amount(2000)
                .wallet(wallet)
                .build();
        WalletOperationResponse expected = new WalletOperationResponse(
                id, WalletOperationType.INCREASE, 2000, walletResponse
        );

        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        WalletOperationResponse result = walletOperationMapper.toResponse(walletOperation);

        verify(walletMapper).toResponse(wallet);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Wallet wallet1 = WalletFixture.aDefaultWallet();
        Wallet wallet2 = WalletFixture.aDefaultWallet();
        WalletResponse walletResponse1 = WalletResponseFixture.aDefaultWalletResponse();
        WalletResponse walletResponse2 = WalletResponseFixture.aDefaultWalletResponse();
        WalletOperation walletOperation1 = WalletOperation.builder()
                .id(id1)
                .type(WalletOperationType.INCREASE)
                .amount(2000)
                .wallet(wallet1)
                .build();
        WalletOperation walletOperation2 = WalletOperation.builder()
                .id(id2)
                .type(WalletOperationType.INCREASE)
                .amount(2000)
                .wallet(wallet2)
                .build();
        WalletOperationResponse expected1 = new WalletOperationResponse(
                id1, WalletOperationType.INCREASE, 2000, walletResponse1
        );
        WalletOperationResponse expected2 = new WalletOperationResponse(
                id2, WalletOperationType.INCREASE, 2000, walletResponse2
        );
        Page<WalletOperation> walletOperations = new PageImpl<>(List.of(walletOperation1, walletOperation2));
        Page<WalletOperationResponse> expected = new PageImpl<>(List.of(expected1, expected2));

        when(walletMapper.toResponse(wallet1)).thenReturn(walletResponse1);
        when(walletMapper.toResponse(wallet2)).thenReturn(walletResponse2);

        Page<WalletOperationResponse> result = walletOperationMapper.toResponse(walletOperations);

        verify(walletMapper).toResponse(wallet1);
        verify(walletMapper).toResponse(wallet2);

        assertEquals(expected, result);
    }
}
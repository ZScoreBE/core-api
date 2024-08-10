package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.dto.mapper.wallet.WalletOperationMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationRequestFixture;
import be.zsoft.zscore.core.repository.wallet.WalletOperationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletOperationServiceTest {

    @Mock
    private WalletOperationRepo walletOperationRepo;

    @Mock
    private WalletOperationMapper walletOperationMapper;

    @InjectMocks
    private WalletOperationService walletOperationService;

    @Captor
    private ArgumentCaptor<WalletOperation> walletOperationArgumentCaptor;

    @Test
    void createWalletOperation() {
        WalletOperationRequest request = WalletOperationRequestFixture.aDefaultWalletOperationRequest();
        Wallet wallet = WalletFixture.aDefaultWallet();
        WalletOperation expected = WalletOperationFixture.aDefaultWalletOperation();

        when(walletOperationMapper.fromRequest(request)).thenReturn(expected);
        when(walletOperationRepo.saveAndFlush(expected)).thenReturn(expected);

        WalletOperation result = walletOperationService.createWalletOperation(request, wallet);

        verify(walletOperationRepo).saveAndFlush(walletOperationArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(wallet, walletOperationArgumentCaptor.getValue().getWallet());

    }

    @Test
    void getWalletOperationsByWallet() {
        Wallet wallet = WalletFixture.aDefaultWallet();
        Pageable pageable = PageRequest.of(1, 10);
        Page<WalletOperation> expected = new PageImpl<>(List.of(
                WalletOperationFixture.aDefaultWalletOperation(),
                WalletOperationFixture.aDefaultWalletOperation()
        ));

        when(walletOperationRepo.findAllByWallet(wallet, pageable)).thenReturn(expected);

        Page<WalletOperation> result = walletOperationService.getWalletOperationsByWallet(wallet, pageable);

        verify(walletOperationRepo).findAllByWallet(wallet, pageable);

        assertEquals(expected, result);
    }
}
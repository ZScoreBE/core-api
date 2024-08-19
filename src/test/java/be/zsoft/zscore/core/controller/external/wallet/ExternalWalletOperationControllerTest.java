package be.zsoft.zscore.core.controller.external.wallet;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletOperationMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletOperationResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationRequestFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationResponseFixture;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletOperationService;
import be.zsoft.zscore.core.service.wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalWalletOperationControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private WalletService walletService;

    @Mock
    private WalletOperationService walletOperationService;

    @Mock
    private WalletOperationMapper walletOperationMapper;

    @InjectMocks
    private ExternalWalletOperationController externalWalletOperationController;

    @Test
    void createWalletOperation() {
        UUID walletId = UUID.randomUUID();
        WalletOperationRequest request = WalletOperationRequestFixture.aDefaultWalletOperationRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        WalletOperation walletOperation = WalletOperationFixture.aDefaultWalletOperation();
        WalletOperationResponse expected = WalletOperationResponseFixture.aDefaultWalletOperationResponse();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(walletService.updateWalletAmount(walletId, player, request)).thenReturn(wallet);
        when(walletOperationService.createWalletOperation(request, wallet)).thenReturn(walletOperation);
        when(walletOperationMapper.toResponse(walletOperation)).thenReturn(expected);

        WalletOperationResponse result = externalWalletOperationController.createWalletOperation(walletId, request);

        verify(playerService).getAuthenticatedPlayer();
        verify(walletService).updateWalletAmount(walletId, player, request);
        verify(walletOperationService).createWalletOperation(request, wallet);
        verify(walletOperationMapper).toResponse(walletOperation);

        assertEquals(expected, result);
    }

    @Test
    void getOperations() {
        UUID walletId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(1, 10);
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        Page<WalletOperation> operations = new PageImpl<>(List.of(
                WalletOperationFixture.aDefaultWalletOperation(),
                WalletOperationFixture.aDefaultWalletOperation()
        ));
        List<WalletOperationResponse> expected = List.of(
                WalletOperationResponseFixture.aDefaultWalletOperationResponse(),
                WalletOperationResponseFixture.aDefaultWalletOperationResponse()
        );

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(walletService.getWalletById(walletId, player)).thenReturn(wallet);
        when(walletOperationService.getWalletOperationsByWallet(wallet, pageable)).thenReturn(operations);
        when(walletOperationMapper.toResponse(operations)).thenReturn(new PageImpl<>(expected));

        PaginatedResponse<WalletOperationResponse> result = externalWalletOperationController.getOperations(walletId, pageable);

        verify(playerService).getAuthenticatedPlayer();
        verify(walletService).getWalletById(walletId, player);
        verify(walletOperationMapper).toResponse(operations);

        assertEquals(expected, result.items());
    }
}
package be.zsoft.zscore.core.controller.external.wallet;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletRequestFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletResponseFixture;
import be.zsoft.zscore.core.service.player.PlayerService;
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
class ExternalWalletControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private WalletService walletService;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private ExternalWalletController externalWalletController;

    @Test
    void createWallet() {
        WalletRequest request = WalletRequestFixture.aDefaultWalletRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        WalletResponse expected = WalletResponseFixture.aDefaultWalletResponse();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(walletService.createWallet(request, player)).thenReturn(wallet);
        when(walletMapper.toResponse(wallet)).thenReturn(expected);

        WalletResponse response = externalWalletController.createWallet(request);

        verify(playerService).getAuthenticatedPlayer();
        verify(walletService).createWallet(request, player);
        verify(walletMapper).toResponse(wallet);

        assertEquals(expected, response);
    }

    @Test
    void getWallets() {
        Pageable pageable = PageRequest.of(1, 10);
        Player player = PlayerFixture.aDefaultPlayer();
        Page<Wallet> wallets = new PageImpl<>(List.of(
           WalletFixture.aDefaultWallet(),
           WalletFixture.aDefaultWallet()
        ));
        List<WalletResponse> expected = List.of(
                WalletResponseFixture.aDefaultWalletResponse(),
                WalletResponseFixture.aDefaultWalletResponse()
        );

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(walletService.getWalletsByPlayer(player, pageable)).thenReturn(wallets);
        when(walletMapper.toResponse(wallets)).thenReturn(new PageImpl<>(expected));

        PaginatedResponse<WalletResponse> result = externalWalletController.getWallets(pageable);

        verify(playerService).getAuthenticatedPlayer();
        verify(walletService).getWalletsByPlayer(player, pageable);
        verify(walletMapper).toResponse(wallets);

        assertEquals(expected, result.items());
    }

    @Test
    void getWalletById() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        WalletResponse expected = WalletResponseFixture.aDefaultWalletResponse();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(walletService.getWalletById(id, player)).thenReturn(wallet);
        when(walletMapper.toResponse(wallet)).thenReturn(expected);

        WalletResponse result = externalWalletController.getWalletById(id);

        verify(playerService).getAuthenticatedPlayer();
        verify(walletService).getWalletById(id, player);
        verify(walletMapper).toResponse(wallet);

        assertEquals(expected, result);
    }
}
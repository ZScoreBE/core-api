package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperationType;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletOperationRequestFixture;
import be.zsoft.zscore.core.repository.wallet.WalletRepo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private WalletOperationService walletOperationService;

    @InjectMocks
    private WalletService walletService;

    @Captor
    private ArgumentCaptor<List<Wallet>> walletsArgumentCaptor;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Test
    void createWallet_playersList() {
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Player player1 = PlayerFixture.aDefaultPlayer();
        Player player2 = PlayerFixture.aDefaultPlayer();
        List<Player> players = List.of(player1, player2);
        List<Wallet> expected = List.of(
                Wallet.builder().amount(0).player(player1).currency(currency).build(),
                Wallet.builder().amount(0).player(player2).currency(currency).build()
        );

        walletService.createWallets(currency, players);

        verify(walletRepo).saveAllAndFlush(walletsArgumentCaptor.capture());

        assertEquals(expected, walletsArgumentCaptor.getValue());
    }

    @Test
    void createWallet_currencyList() {
        Currency currency1 = CurrencyFixture.aDefaultCurrency();
        Currency currency2 = CurrencyFixture.aDefaultCurrency();
        Player player = PlayerFixture.aDefaultPlayer();
        List<Currency> currencies = List.of(currency1, currency2);
        List<Wallet> expected = List.of(
                Wallet.builder().amount(0).player(player).currency(currency1).build(),
                Wallet.builder().amount(0).player(player).currency(currency2).build()
        );

        walletService.createWallets(currencies, player);

        verify(walletRepo).saveAllAndFlush(walletsArgumentCaptor.capture());

        assertEquals(expected, walletsArgumentCaptor.getValue());
    }

    @Test
    void getWalletsByPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();
        Pageable pageable = PageRequest.of(1, 10);

        Page<Wallet> expected = new PageImpl<>(List.of(
                WalletFixture.aDefaultWallet(),
                WalletFixture.aDefaultWallet()
        ));

        when(walletRepo.findAllByPlayer(player, pageable)).thenReturn(expected);

        Page<Wallet> result = walletService.getWalletsByPlayer(player, pageable);

        assertEquals(expected, result);
    }

    @Test
    void getWalletById_success() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet expected = WalletFixture.aDefaultWallet();

        when(walletRepo.findByIdAndPlayer(id, player)).thenReturn(Optional.of(expected));

        Wallet result = walletService.getWalletById(id, player);

        verify(walletRepo).findByIdAndPlayer(id, player);

        assertEquals(expected, result);
    }

    @Test
    void getWalletById_notFound() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();

        when(walletRepo.findByIdAndPlayer(id, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> walletService.getWalletById(id, player));

        verify(walletRepo).findByIdAndPlayer(id, player);
    }

    @Test
    void updateWalletAmount_decrease_toMuch() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(100);
        WalletOperationRequest request = WalletOperationRequestFixture.aDecreaseWalletOperation();

        when(walletRepo.findByIdAndPlayer(id, player)).thenReturn(Optional.of(wallet));

        ApiException result = assertThrows(ApiException.class, () -> walletService.updateWalletAmount(id, player, request));

        verify(walletOperationService, never()).createWalletOperation(request, wallet);
        verify(walletRepo, never()).saveAndFlush(wallet);

        assertEquals(ErrorCodes.WALLET_DOES_NOT_HAVE_ENOUGH_AMOUNT, result.getErrorKey());
    }

    @Test
    void updateWalletAmount_decrease_success() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(5000);
        WalletOperationRequest request = WalletOperationRequestFixture.aDecreaseWalletOperation();
        Wallet expected = WalletFixture.aDefaultWallet();

        when(walletRepo.findByIdAndPlayer(id, player)).thenReturn(Optional.of(wallet));
        when(walletRepo.saveAndFlush(any(Wallet.class))).thenReturn(expected);

        Wallet result = walletService.updateWalletAmount(id, player, request);

        verify(walletOperationService).createWalletOperation(eq(request), any(Wallet.class));
        verify(walletRepo).saveAndFlush(walletCaptor.capture());

        assertEquals(expected, result);
        assertEquals(3000, walletCaptor.getValue().getAmount());
    }

    @Test
    void updateWalletAmount_increase() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(5000);
        WalletOperationRequest request = WalletOperationRequestFixture.aDefaultWalletOperationRequest();
        Wallet expected = WalletFixture.aDefaultWallet();

        when(walletRepo.findByIdAndPlayer(id, player)).thenReturn(Optional.of(wallet));
        when(walletRepo.saveAndFlush(any(Wallet.class))).thenReturn(expected);

        Wallet result = walletService.updateWalletAmount(id, player, request);

        verify(walletOperationService).createWalletOperation(eq(request), any(Wallet.class));
        verify(walletRepo).saveAndFlush(walletCaptor.capture());

        assertEquals(expected, result);
        assertEquals(7000, walletCaptor.getValue().getAmount());
    }

    @Test
    void takeCurrency_notFound() {
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();

        when(walletRepo.findByCurrencyAndPlayer(currency, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> walletService.takeCurrency(player, currency, 2000));

        verify(walletOperationService, never()).createWalletOperation(any(WalletOperationRequest.class), any(Wallet.class));
        verify(walletRepo, never()).saveAndFlush(any(Wallet.class));
    }

    @Test
    void takeCurrency_notEnoughAmount() {
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(2000);

        when(walletRepo.findByCurrencyAndPlayer(currency, player)).thenReturn(Optional.of(wallet));

        ApiException result = assertThrows(ApiException.class, () -> walletService.takeCurrency(player, currency, 5000));

        verify(walletOperationService, never()).createWalletOperation(new WalletOperationRequest(WalletOperationType.DECREASE, 5000), wallet);
        verify(walletRepo, never()).saveAndFlush(wallet);

        assertEquals(ErrorCodes.WALLET_DOES_NOT_HAVE_ENOUGH_AMOUNT, result.getErrorKey());
    }

    @Test
    void takeCurrency_success() {
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(5000);
        WalletOperationRequest request = WalletOperationRequestFixture.aDecreaseWalletOperation();

        when(walletRepo.findByCurrencyAndPlayer(currency, player)).thenReturn(Optional.of(wallet));

        walletService.takeCurrency(player, currency, 4000);

        verify(walletOperationService).createWalletOperation(eq(new WalletOperationRequest(WalletOperationType.DECREASE, 4000)), any(Wallet.class));
        verify(walletRepo).saveAndFlush(walletCaptor.capture());

        assertEquals(1000, walletCaptor.getValue().getAmount());
    }

    @Test
    void giveCurrency_notFound() {
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();

        when(walletRepo.findByCurrencyAndPlayer(currency, player)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> walletService.giveCurrency(player, currency, 2000));

        verify(walletOperationService, never()).createWalletOperation(any(WalletOperationRequest.class), any(Wallet.class));
        verify(walletRepo, never()).saveAndFlush(any(Wallet.class));
    }

    @Test
    void giveCurrency_success() {
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Wallet wallet = WalletFixture.aDefaultWallet();
        wallet.setAmount(5000);
        WalletOperationRequest request = WalletOperationRequestFixture.aDecreaseWalletOperation();

        when(walletRepo.findByCurrencyAndPlayer(currency, player)).thenReturn(Optional.of(wallet));

        walletService.giveCurrency(player, currency, 4000);

        verify(walletOperationService).createWalletOperation(eq(new WalletOperationRequest(WalletOperationType.INCREASE, 4000)), any(Wallet.class));
        verify(walletRepo).saveAndFlush(walletCaptor.capture());

        assertEquals(9000, walletCaptor.getValue().getAmount());
    }
}

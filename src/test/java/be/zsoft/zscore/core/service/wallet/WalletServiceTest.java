package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private WalletService walletService;

    @Captor
    private ArgumentCaptor<List<Wallet>> walletsArgumentCaptor;

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
}

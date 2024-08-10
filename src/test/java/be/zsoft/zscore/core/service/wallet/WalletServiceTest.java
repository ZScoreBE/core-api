package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletFixture;
import be.zsoft.zscore.core.fixtures.wallet.WalletRequestFixture;
import be.zsoft.zscore.core.repository.wallet.WalletRepo;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private GameService gameService;

    @InjectMocks
    private WalletService walletService;

    @Captor
    private ArgumentCaptor<Wallet> walletArgumentCaptor;

    @Test
    void createWallet_success() {
        WalletRequest request = WalletRequestFixture.aDefaultWalletRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Game game = GameFixture.aDefaultGame();
        Wallet expected = WalletFixture.aDefaultWallet();

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(currencyService.getCurrencyById(game, request.currencyId())).thenReturn(currency);
        when(walletRepo.existsByPlayerAndCurrency(player, currency)).thenReturn(false);
        when(walletMapper.fromRequest(request)).thenReturn(expected);
        when(walletRepo.saveAndFlush(any(Wallet.class))).thenReturn(expected);

        Wallet result = walletService.createWallet(request, player);

        verify(gameService).getAuthenicatedGame();
        verify(currencyService).getCurrencyById(game, request.currencyId());
        verify(walletRepo).existsByPlayerAndCurrency(player, currency);
        verify(walletMapper).fromRequest(request);
        verify(walletRepo).saveAndFlush(walletArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(currency, walletArgumentCaptor.getValue().getCurrency());
        assertEquals(player, walletArgumentCaptor.getValue().getPlayer());
    }

    @Test
    void createWallet_alreadyExists() {
        WalletRequest request = WalletRequestFixture.aDefaultWalletRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Game game = GameFixture.aDefaultGame();

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(currencyService.getCurrencyById(game, request.currencyId())).thenReturn(currency);
        when(walletRepo.existsByPlayerAndCurrency(player, currency)).thenReturn(true);

        ApiException result = assertThrows(ApiException.class, () -> walletService.createWallet(request, player));

        verify(gameService).getAuthenicatedGame();
        verify(currencyService).getCurrencyById(game, request.currencyId());
        verify(walletRepo).existsByPlayerAndCurrency(player, currency);
        verify(walletMapper, never()).fromRequest(request);
        verify(walletRepo, never()).saveAndFlush(any(Wallet.class));

        assertEquals(ErrorCodes.WALLET_ALREADY_EXISTS, result.getErrorKey());
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

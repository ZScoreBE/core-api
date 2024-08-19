package be.zsoft.zscore.core.service.trigger;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.trigger.TriggerMapper;
import be.zsoft.zscore.core.dto.request.trigger.TriggerRequest;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import be.zsoft.zscore.core.fixtures.currency.CurrencyFixture;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerFixture;
import be.zsoft.zscore.core.fixtures.trigger.TriggerRequestFixture;
import be.zsoft.zscore.core.repository.trigger.TriggerRepo;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletService;
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
class TriggerServiceTest {

    @Mock
    private TriggerRepo triggerRepo;

    @Mock
    private TriggerMapper triggerMapper;

    @Mock
    private WalletService walletService;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private TriggerService triggerService;

    @Captor
    private ArgumentCaptor<Trigger> triggerArgumentCaptor;

    @Test
    void createTrigger() {
        Game game = GameFixture.aDefaultGame();
        TriggerRequest request = TriggerRequestFixture.aDefaultTriggerRequest();
        Trigger expected = TriggerFixture.aDefaultTrigger();

        when(triggerMapper.fromRequest(request, game)).thenReturn(expected);
        when(triggerRepo.saveAndFlush(expected)).thenReturn(expected);

        Trigger result = triggerService.createTrigger(request, game);

        verify(triggerMapper).fromRequest(request, game);
        verify(triggerRepo).saveAndFlush(triggerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(game, triggerArgumentCaptor.getValue().getGame());
    }

    @Test
    void getTriggersByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Trigger> expected = new PageImpl<>(List.of(
                TriggerFixture.aDefaultTrigger(),
                TriggerFixture.aDefaultTrigger()
        ));

        when(triggerRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Trigger> result = triggerService.getTriggersByGame(game, pageable);

        verify(triggerRepo).findAllByGame(game, pageable);

        assertEquals(expected, result);
    }

    @Test
    void searchTriggersByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Trigger> expected = new PageImpl<>(List.of(
                TriggerFixture.aDefaultTrigger(),
                TriggerFixture.aDefaultTrigger()
        ));

        when(triggerRepo.searchAllByGame("%search%", game, pageable)).thenReturn(expected);

        Page<Trigger> result = triggerService.searchTriggersByGame("search", game, pageable);

        verify(triggerRepo).searchAllByGame("%search%", game, pageable);

        assertEquals(expected, result);
    }

    @Test
    void getTriggerByIdAndGame_success() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Trigger expected = TriggerFixture.aDefaultTrigger();

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));

        Trigger result =  triggerService.getTriggerByIdAndGame(id, game);

        verify(triggerRepo).findByIdAndGame(id, game);

        assertEquals(expected, result);
    }

    @Test
    void getTriggerByIdAndGame_notFound() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> triggerService.getTriggerByIdAndGame(id, game));

        verify(triggerRepo).findByIdAndGame(id, game);
    }

    @Test
    void updateTrigger() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        TriggerRequest request = TriggerRequestFixture.aDefaultTriggerRequest();
        Trigger expected = TriggerFixture.aDefaultTrigger();

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));
        when(triggerMapper.fromRequest(request, game, expected)).thenReturn(expected);
        when(triggerRepo.saveAndFlush(expected)).thenReturn(expected);

        Trigger result = triggerService.updateTrigger(request, id, game);

        verify(triggerRepo).findByIdAndGame(id, game);
        verify(triggerMapper).fromRequest(request, game, expected);
        verify(triggerRepo).saveAndFlush(expected);

        assertEquals(expected, result);
    }

    @Test
    void deleteByIdAndGame() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Trigger expected = TriggerFixture.aDefaultTrigger();

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(expected));

        triggerService.deleteByIdAndGame(id, game);

        verify(triggerRepo).findByIdAndGame(id, game);
        verify(triggerRepo).delete(expected);
    }

    @Test
    void executeTrigger_costFree_rewardCurrency() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Player player = PlayerFixture.aDefaultPlayer();
        player.setGame(game);
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Trigger trigger = TriggerFixture.aCostFreeRewardCurrencyTrigger();
        trigger.setCostCurrency(currency);
        trigger.setRewardCurrency(currency);

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(trigger));

        triggerService.executeTrigger(id, player);

        verify(walletService, never()).takeCurrency(player, currency, 100);
        verify(playerService, never()).giveLives(anyInt());
        verify(walletService).giveCurrency(player, currency, 100);
    }

    @Test
    void executeTrigger_costCurrency_rewardLives() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Player player = PlayerFixture.aDefaultPlayer();
        player.setGame(game);
        Currency currency = CurrencyFixture.aDefaultCurrency();
        Trigger trigger = TriggerFixture.aCostCurrencyRewardLivesTrigger();
        trigger.setCostCurrency(currency);

        when(triggerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(trigger));

        triggerService.executeTrigger(id, player);

        verify(walletService).takeCurrency(player, currency, 100);
        verify(playerService).giveLives(100);
        verify(walletService, never()).giveCurrency(player, currency, 100);
    }
}
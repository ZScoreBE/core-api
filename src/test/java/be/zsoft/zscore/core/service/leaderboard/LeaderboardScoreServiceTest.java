package be.zsoft.zscore.core.service.leaderboard;

import be.zsoft.zscore.core.dto.request.leaderboard.ExternalLeaderboardScoreRequest;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardScoreRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardScoreFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.repository.leaderboard.LeaderboardScoreRepo;
import be.zsoft.zscore.core.service.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardScoreServiceTest {

    @Mock
    private LeaderboardScoreRepo leaderboardScoreRepo;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private LeaderboardScoreService leaderboardScoreService;

    @Captor
    private ArgumentCaptor<LeaderboardScore> leaderboardScoreCaptor;

    @Test
    void createLeaderboardScore_external_highest_noPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(100);
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.empty());
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(100, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_external_highest_scoreLowerThenOldOne() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(100);
        LeaderboardScore expected = LeaderboardScoreFixture.withScoreLeaderboard(300);

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(expected));

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo, never()).saveAndFlush(any(LeaderboardScore.class));
    }

    @Test
    void createLeaderboardScore_external_highest_scoreHigherThenOldOne() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(200);
        LeaderboardScore existing = LeaderboardScore.builder().id(UUID.randomUUID()).score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(existing));
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNotNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(200, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_internal_highest_noPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID playerId = player.getId();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(100, playerId);
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.empty());
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(100, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_internal_highest_scoreLowerThenOldOne() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID playerId = player.getId();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(100, playerId);
        LeaderboardScore expected = LeaderboardScoreFixture.withScoreLeaderboard(300);

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(expected));

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo, never()).saveAndFlush(any(LeaderboardScore.class));
    }

    @Test
    void createLeaderboardScore_internal_highest_scoreHigherThenOldOne() {
        UUID playerId = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aHighestScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(200, playerId);
        LeaderboardScore existing = LeaderboardScore.builder().id(UUID.randomUUID()).score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(existing));
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNotNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(200, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_external_latest_noPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aLatestScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(100);
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.empty());
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(100, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_external_latest_withPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aLatestScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(50);
        LeaderboardScore existing = LeaderboardScore.builder().id(UUID.randomUUID()).score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(existing));
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNotNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(50, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_internal_latest_noPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID playerId = player.getId();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aLatestScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(100, playerId);
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.empty());
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(100, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_internal_latest_withPreviousScore() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID playerId = player.getId();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aLatestScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(50, playerId);
        LeaderboardScore existing = LeaderboardScore.builder().id(UUID.randomUUID()).score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.findByLeaderboardAndPlayer(leaderboard, player)).thenReturn(Optional.of(existing));
        when(leaderboardScoreRepo.saveAndFlush(any(LeaderboardScore.class))).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).findByLeaderboardAndPlayer(leaderboard, player);
        verify(leaderboardScoreRepo).saveAndFlush(leaderboardScoreCaptor.capture());

        assertNotNull(leaderboardScoreCaptor.getValue().getId());
        assertEquals(50, leaderboardScoreCaptor.getValue().getScore());
        assertEquals(player, leaderboardScoreCaptor.getValue().getPlayer());
        assertEquals(leaderboard, leaderboardScoreCaptor.getValue().getLeaderboard());
    }

    @Test
    void createLeaderboardScore_external_multiple() {
        Player player = PlayerFixture.aDefaultPlayer();
        Leaderboard leaderboard = LeaderboardFixture.aMultipleScoreTypeLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(100);
        LeaderboardScore toSave = LeaderboardScore.builder().score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getAuthenticatedPlayer()).thenReturn(player);
        when(leaderboardScoreRepo.saveAndFlush(toSave)).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getAuthenticatedPlayer();
        verify(leaderboardScoreRepo).saveAndFlush(toSave);
    }

    @Test
    void createLeaderboardScore_internal_multiple() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID playerId = player.getId();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aMultipleScoreTypeLeaderboard();
        LeaderboardScoreRequest request = new LeaderboardScoreRequest(100, playerId);
        LeaderboardScore toSave = LeaderboardScore.builder().score(100).player(player).leaderboard(leaderboard).build();
        LeaderboardScore expected = LeaderboardScoreFixture.aDefaultLeaderboardScore();

        when(playerService.getPlayerByIdAndGame(playerId, game)).thenReturn(player);
        when(leaderboardScoreRepo.saveAndFlush(toSave)).thenReturn(expected);

        LeaderboardScore result = leaderboardScoreService.createLeaderboardScore(game, leaderboard, request);

        assertEquals(expected, result);

        verify(playerService).getPlayerByIdAndGame(playerId, game);
        verify(leaderboardScoreRepo).saveAndFlush(toSave);
    }

    @Test
    void getScores() {
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        Pageable pageable = PageRequest.of(1, 10, Sort.Direction.ASC, "score");
        Page<LeaderboardScore> expected = new PageImpl<>(List.of(
                LeaderboardScoreFixture.aDefaultLeaderboardScore(),
                LeaderboardScoreFixture.aDefaultLeaderboardScore()
        ));

        when(leaderboardScoreRepo.findAllScoresByLeaderboard(leaderboard, pageable)).thenReturn(expected);

        Page<LeaderboardScore> result = leaderboardScoreService.getScores(leaderboard, pageable);

        assertEquals(expected, result);

        verify(leaderboardScoreRepo).findAllScoresByLeaderboard(leaderboard, pageable);
    }

    @Test
    void deleteAllScores() {
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();

        leaderboardScoreService.deleteAllScores(leaderboard);

        verify(leaderboardScoreRepo).deleteAllByLeaderboard(leaderboard);
    }

    @Test
    void deleteAllScoresByPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();

        leaderboardScoreService.deleteAllScoresByPlayer(player);

        verify(leaderboardScoreRepo).deleteAllByPlayer(player);
    }
}
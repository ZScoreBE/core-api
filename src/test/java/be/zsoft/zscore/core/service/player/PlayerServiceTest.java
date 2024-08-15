package be.zsoft.zscore.core.service.player;

import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerLifeSettingsFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerRequestFixtures;
import be.zsoft.zscore.core.repository.player.PlayerRepo;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.game.GameService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepo playerRepo;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private GameService gameService;

    @Mock
    private Clock clock;

    @Mock
    private PlayerLifeSettingsService playerLifeSettingsService;

    @InjectMocks
    private PlayerService playerService;

    @Captor
    private ArgumentCaptor<Player> playerArgumentCaptor;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createPlayer() {
        PlayerRequest request = PlayerRequestFixtures.aDefaultPlayerRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        Game game = GameFixture.aDefaultGame();

        when(playerMapper.fromRequest(request)).thenReturn(player);
        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(player);
        mockLocalDateTimeNow();

        Player result = playerService.createPlayer(request);

        verify(playerMapper).fromRequest(request);
        verify(gameService).getAuthenicatedGame();
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(player, result);
        assertEquals(game, playerArgumentCaptor.getValue().getGame());
        assertEquals(LocalDateTime.of(2000, 1, 1, 10, 30), playerArgumentCaptor.getValue().getLastSignIn());
    }

    @Test
    void getPlayerById_success() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();

        when(playerRepo.findById(id)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById(id);

        verify(playerRepo).findById(id);

        assertEquals(player, result);
    }

    @Test
    void getPlayerById_notFound() {
        UUID id = UUID.randomUUID();

        when(playerRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> playerService.getPlayerById(id));

        verify(playerRepo).findById(id);
    }

    @Test
    void getPlayerByIdAndGame_success() {
        UUID id = UUID.randomUUID();
        Player player = PlayerFixture.aDefaultPlayer();
        Game game = GameFixture.aDefaultGame();

        when(playerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerByIdAndGame(id, game);

        verify(playerRepo).findByIdAndGame(id, game);

        assertEquals(player, result);
    }

    @Test
    void getPlayerByIdAndGame_notFound() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(playerRepo.findByIdAndGame(id, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> playerService.getPlayerByIdAndGame(id, game));

        verify(playerRepo).findByIdAndGame(id, game);
    }

    @Test
    void getPlayersByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> expected = new PageImpl<>(List.of(
                PlayerFixture.aDefaultPlayer(),
                PlayerFixture.aDefaultPlayer()
        ));

        when(playerRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Player> result = playerService.getPlayersByGame(game, pageable);

        assertEquals(expected, result);

        verify(playerRepo).findAllByGame(game, pageable);
    }

    @Test
    void searchPlayersByGame() {
        String search = "search";
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> expected = new PageImpl<>(List.of(
                PlayerFixture.aDefaultPlayer(),
                PlayerFixture.aDefaultPlayer()
        ));

        when(playerRepo.searchAllOnNameByGame("%" + search + "%", game, pageable)).thenReturn(expected);

        Page<Player> result = playerService.searchPlayersByGame(search, game, pageable);

        assertEquals(expected, result);

        verify(playerRepo).searchAllOnNameByGame("%" + search + "%", game, pageable);
    }

    @Test
    void rawUpdate() {
        Player player = PlayerFixture.aDefaultPlayer();

        playerService.rawUpdate(player);

        verify(playerRepo).saveAndFlush(player);
    }

    @Test
    void getAuthenticatedPlayer_success() {
        Player expected = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Player result = playerService.getAuthenticatedPlayer();

        assertEquals(expected, result);
    }

    @Test
    void getAuthenticatedPlayer_noPlayerFound() {
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, null,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(NotFoundException.class, () -> playerService.getAuthenticatedPlayer());
    }

    @Test
    void countPlayersByGame() {
        Game game = GameFixture.aDefaultGame();
        long expected = 10;

        when(playerRepo.countByGame(game)).thenReturn(expected);

        long result = playerService.countPlayersByGame(game);

        assertEquals(expected, result);
        verify(playerRepo).countByGame(game);
    }

    @Test
    void deletePlayer() {
        Player player = PlayerFixture.aDefaultPlayer();

        playerService.deletePlayer(player);

        verify(playerRepo).delete(player);
    }

    @Test
    void getAllPlayersByGame() {
        Game game = GameFixture.aDefaultGame();
        List<Player> expected = List.of(
                PlayerFixture.aDefaultPlayer(),
                PlayerFixture.aDefaultPlayer()
        );

        when(playerRepo.findAllByGame(game)).thenReturn(expected);

        List<Player> result = playerService.getAllPlayersByGame(game);

        verify(playerRepo).findAllByGame(game);

        assertEquals(expected, result);
    }

    @Test
    void updateAuthenticatedPlayerLivesOnCount_disabled() {
        Player expected = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDisabledPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.updateAuthenticatedPlayerLivesOnCount();

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertNull(playerArgumentCaptor.getValue().getCurrentLives());
        assertNull(playerArgumentCaptor.getValue().getLastLifeUpdate());

    }

    @Test
    void updateAuthenticatedPlayerLivesOnCount_enabled_giveLifeAfterSecondsNull() {
        Player expected = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aPlayerLifeSettingsWithNoAutomaticLives();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));

        Player result = playerService.updateAuthenticatedPlayerLivesOnCount();

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo, never()).saveAndFlush(any(Player.class));

        assertEquals(expected, result);
    }

    @Test
    void updateAuthenticatedPlayerLivesOnCount_enabled_currentLivesAndLastLifeUpdateNull() {
        Player expected = PlayerFixture.aPlayerWithCurrentLivesAndLastLifeUpdateNull();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);
        mockLocalDateTimeNow();

        Player result = playerService.updateAuthenticatedPlayerLivesOnCount();

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(10, playerArgumentCaptor.getValue().getCurrentLives());
        assertEquals(LocalDateTime.now(clock), playerArgumentCaptor.getValue().getLastLifeUpdate());
    }

    @Test
    void updateAuthenticatedPlayerLivesOnCount_enabled_normalUpdate() {
        mockLocalDateTimeNow();

        LocalDateTime now = LocalDateTime.now(clock);
        Player expected = PlayerFixture.aDefaultPlayer();
        expected.setCurrentLives(1);
        expected.setLastLifeUpdate(now.minusSeconds(2000)); // 2 Lives & 200 seconds rest

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.updateAuthenticatedPlayerLivesOnCount();

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(3, playerArgumentCaptor.getValue().getCurrentLives());
        assertEquals(LocalDateTime.now(clock).minusSeconds(200), playerArgumentCaptor.getValue().getLastLifeUpdate());
    }

    @Test
    void takeLives_amountWrong() {
        ApiException ex = assertThrows(ApiException.class, () -> playerService.takeLives(-1));

        verify(playerLifeSettingsService, never()).getPlayerLifeSettingsAsOptional(any(Game.class));
        verify(playerRepo, never()).saveAndFlush(any(Player.class));

        assertEquals("TAKE_LIFE_AMOUNT_MIN_NEEDS_TO_BE_1", ex.getErrorKey());
    }

    @Test
    void takeLives_playerLifeNotEnabled() {
        Player expected = PlayerFixture.aPlayerWithCurrentLivesAndLastLifeUpdateNull();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDisabledPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));

        ApiException ex = assertThrows(ApiException.class, () -> playerService.takeLives(1));

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo, never()).saveAndFlush(any(Player.class));

        assertEquals("PLAYER_LIFE_NOT_ENABLED", ex.getErrorKey());
    }

    @Test
    void takeLives_takeToZero() {
        mockLocalDateTimeNow();

        Player expected = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.takeLives(1000);

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(0, playerArgumentCaptor.getValue().getCurrentLives());
    }

    @Test
    void takeLives_normalUpdate() {
        mockLocalDateTimeNow();

        Player expected = PlayerFixture.aDefaultPlayer();
        expected.setCurrentLives(2);
        expected.setLastLifeUpdate(LocalDateTime.now(clock));
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.takeLives(1);

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(1, playerArgumentCaptor.getValue().getCurrentLives());
    }

    @Test
    void giveLives_amountWrong() {
        ApiException ex = assertThrows(ApiException.class, () -> playerService.giveLives(-1));

        verify(playerLifeSettingsService, never()).getPlayerLifeSettingsAsOptional(any(Game.class));
        verify(playerRepo, never()).saveAndFlush(any(Player.class));

        assertEquals("GIVE_LIFE_AMOUNT_MIN_NEEDS_TO_BE_1", ex.getErrorKey());
    }

    @Test
    void giveLives_playerLifeNotEnabled() {
        Player expected = PlayerFixture.aPlayerWithCurrentLivesAndLastLifeUpdateNull();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDisabledPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));

        ApiException ex = assertThrows(ApiException.class, () -> playerService.giveLives(1));

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo, never()).saveAndFlush(any(Player.class));

        assertEquals("PLAYER_LIFE_NOT_ENABLED", ex.getErrorKey());
    }

    @Test
    void giveLives_maxLives() {
        mockLocalDateTimeNow();

        Player expected = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.giveLives(1000);

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(10, playerArgumentCaptor.getValue().getCurrentLives());
    }

    @Test
    void giveLives_normalUpdate() {
        mockLocalDateTimeNow();

        Player expected = PlayerFixture.aDefaultPlayer();
        expected.setCurrentLives(2);
        expected.setLastLifeUpdate(LocalDateTime.now(clock));
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, expected,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        PlayerLifeSettings settings = PlayerLifeSettingsFixture.aDefaultPlayerLifeSettings();
        when(playerLifeSettingsService.getPlayerLifeSettingsAsOptional(expected.getGame())).thenReturn(Optional.of(settings));
        when(playerRepo.saveAndFlush(any(Player.class))).thenReturn(expected);

        Player result = playerService.giveLives(2);

        verify(playerLifeSettingsService).getPlayerLifeSettingsAsOptional(expected.getGame());
        verify(playerRepo).saveAndFlush(playerArgumentCaptor.capture());

        assertEquals(expected, result);
        assertEquals(4, playerArgumentCaptor.getValue().getCurrentLives());
    }

    private void mockLocalDateTimeNow() {
        when(clock.instant()).thenReturn(Instant.parse("2000-01-01T10:30:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }
}
package be.zsoft.zscore.core.service;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.repository.player.PlayerRepo;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        PlayerRequest request = new PlayerRequest("wout");
        Player player = Player.builder().id(UUID.randomUUID()).build();
        Game game = Game.builder().id(UUID.randomUUID()).build();

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
        Player player = Player.builder().id(id).build();

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
        Player player = Player.builder().id(id).build();
        Game game = Game.builder().id(UUID.randomUUID()).build();

        when(playerRepo.findByIdAndGame(id, game)).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerByIdAndGame(id, game);

        verify(playerRepo).findByIdAndGame(id, game);

        assertEquals(player, result);
    }

    @Test
    void getPlayerByIdAndGame_notFound() {
        UUID id = UUID.randomUUID();
        Game game = Game.builder().id(UUID.randomUUID()).build();

        when(playerRepo.findByIdAndGame(id, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> playerService.getPlayerByIdAndGame(id, game));

        verify(playerRepo).findByIdAndGame(id, game);
    }

    @Test
    void getPlayersByGame() {
        Game game = Game.builder().id(UUID.randomUUID()).build();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> expected = new PageImpl<>(List.of(
           Player.builder().id(UUID.randomUUID()).build(),
           Player.builder().id(UUID.randomUUID()).build()
        ));

        when(playerRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Player> result = playerService.getPlayersByGame(game, pageable);

        assertEquals(expected, result);

        verify(playerRepo).findAllByGame(game, pageable);
    }

    @Test
    void searchPlayersByGame() {
        String search = "search";
        Game game = Game.builder().id(UUID.randomUUID()).build();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> expected = new PageImpl<>(List.of(
                Player.builder().id(UUID.randomUUID()).build(),
                Player.builder().id(UUID.randomUUID()).build()
        ));

        when(playerRepo.searchAllOnNameByGame("%" + search + "%", game, pageable)).thenReturn(expected);

        Page<Player> result = playerService.searchPlayersByGame(search, game, pageable);

        assertEquals(expected, result);

        verify(playerRepo).searchAllOnNameByGame("%" + search + "%", game, pageable);
    }

    @Test
    void rawUpdate() {
        Player player = Player.builder().id(UUID.randomUUID()).build();

        playerService.rawUpdate(player);

        verify(playerRepo).saveAndFlush(player);
    }

    @Test
    void getAuthenticatedPlayer_success() {
        Player expected = Player.builder().id(UUID.randomUUID()).build();
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
        Game game = Game.builder().id(UUID.randomUUID()).build();
        long expected = 10;

        when(playerRepo.countByGame(game)).thenReturn(expected);

        long result = playerService.countPlayersByGame(game);

        assertEquals(expected, result);
        verify(playerRepo).countByGame(game);
    }

    @Test
    void deletePlayer() {
        Player player = Player.builder().id(UUID.randomUUID()).build();

        playerService.deletePlayer(player);

        verify(playerRepo).delete(player);
    }

    private void mockLocalDateTimeNow() {
        when(clock.instant()).thenReturn(Instant.parse("2000-01-01T10:30:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }
}
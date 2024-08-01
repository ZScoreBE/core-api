package be.zsoft.zscore.core.controller.player;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerResponseFixture;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardScoreService;
import be.zsoft.zscore.core.service.player.PlayerService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private LeaderboardScoreService leaderboardScoreService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void getPlayers_get() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> players = new PageImpl<>(List.of(
                PlayerFixture.aDefaultPlayer(),
                PlayerFixture.aDefaultPlayer()
        ));
        Page<PlayerResponse> expected = new PageImpl<>(List.of(
                PlayerResponseFixture.aDefaultPlayerResponse(),
                PlayerResponseFixture.aDefaultPlayerResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerService.getPlayersByGame(game, pageable)).thenReturn(players);
        when(playerMapper.toResponse(players)).thenReturn(expected);

        PaginatedResponse<PlayerResponse> result = playerController.getPlayers(gameId, null, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(playerService).getPlayersByGame(game, pageable);
        verify(playerService, never()).searchPlayersByGame(anyString(), eq(game), eq(pageable));
        verify(playerMapper).toResponse(players);
    }

    @Test
    void getPlayers_search() {
        UUID gameId = UUID.randomUUID();
        String search = "search";
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Player> players = new PageImpl<>(List.of(
                PlayerFixture.aDefaultPlayer(),
                PlayerFixture.aDefaultPlayer()
        ));
        Page<PlayerResponse> expected = new PageImpl<>(List.of(
                PlayerResponseFixture.aDefaultPlayerResponse(),
                PlayerResponseFixture.aDefaultPlayerResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerService.searchPlayersByGame(search, game, pageable)).thenReturn(players);
        when(playerMapper.toResponse(players)).thenReturn(expected);

        PaginatedResponse<PlayerResponse> result = playerController.getPlayers(gameId, search, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(playerService, never()).getPlayersByGame(game, pageable);
        verify(playerService).searchPlayersByGame(search, game, pageable);
        verify(playerMapper).toResponse(players);
    }

    @Test
    void countPlayers() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        CountResponse expected = new CountResponse(10L);

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerService.countPlayersByGame(game)).thenReturn(10L);

        CountResponse result = playerController.countPlayers(gameId);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(playerService).countPlayersByGame(game);
    }

    @Test
    void deletePlayer() {
        UUID gameId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Player player = PlayerFixture.aDefaultPlayer();

        when(gameService.getById(gameId)).thenReturn(game);
        when(playerService.getPlayerByIdAndGame(id, game)).thenReturn(player);

        playerController.deletePlayer(gameId, id);

        verify(gameService).getById(gameId);
        verify(playerService).getPlayerByIdAndGame(id, game);
        verify(achievementProgressService).deleteAllProgressesByPlayer(player);
        verify(leaderboardScoreService).deleteAllScoresByPlayer(player);
        verify(playerService).deletePlayer(player);
    }
}
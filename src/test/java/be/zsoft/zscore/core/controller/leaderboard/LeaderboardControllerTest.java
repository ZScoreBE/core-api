package be.zsoft.zscore.core.controller.leaderboard;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardScoreMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.dto.response.common.CountResponse;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.*;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardScoreService;
import be.zsoft.zscore.core.service.leaderboard.LeaderboardService;
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
class LeaderboardControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private LeaderboardScoreService leaderboardScoreService;

    @Mock
    private LeaderboardMapper leaderboardMapper;

    @Mock
    private LeaderboardScoreMapper leaderboardScoreMapper;

    @InjectMocks
    private LeaderboardController leaderboardController;


    @Test
    void createLeaderboard() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        LeaderboardRequest request = LeaderboardRequestFixture.aDefaultLeaderboardRequest();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        LeaderboardResponse expected = LeaderboardResponseFixture.aDefaultLeaderboardResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.createLeaderboard(game, request)).thenReturn(leaderboard);
        when(leaderboardMapper.toResponse(leaderboard)).thenReturn(expected);

        LeaderboardResponse result = leaderboardController.createLeaderboard(gameId, request);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(leaderboardService).createLeaderboard(game, request);
        verify(leaderboardMapper).toResponse(leaderboard);
    }

    @Test
    void getLeaderboards_normal() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Leaderboard> leaderboards = new PageImpl<>(List.of(
                LeaderboardFixture.aDefaultLeaderboard(),
                LeaderboardFixture.aDefaultLeaderboard()
        ));
        Page<LeaderboardResponse> expected = new PageImpl<>(List.of(
                LeaderboardResponseFixture.aDefaultLeaderboardResponse(),
                LeaderboardResponseFixture.aDefaultLeaderboardResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.getLeaderboardsByGame(game,pageable)).thenReturn(leaderboards);
        when(leaderboardMapper.toResponse(leaderboards)).thenReturn(expected);

        PaginatedResponse<LeaderboardResponse> result = leaderboardController.getLeaderboards(gameId, null, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(leaderboardService).getLeaderboardsByGame(game, pageable);
        verify(leaderboardMapper).toResponse(leaderboards);
    }

    @Test
    void getLeaderboards_search() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Leaderboard> leaderboards = new PageImpl<>(List.of(
                LeaderboardFixture.aDefaultLeaderboard(),
                LeaderboardFixture.aDefaultLeaderboard()
        ));
        Page<LeaderboardResponse> expected = new PageImpl<>(List.of(
                LeaderboardResponseFixture.aDefaultLeaderboardResponse(),
                LeaderboardResponseFixture.aDefaultLeaderboardResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.searchLeaderboardsByGame("test", game,pageable)).thenReturn(leaderboards);
        when(leaderboardMapper.toResponse(leaderboards)).thenReturn(expected);

        PaginatedResponse<LeaderboardResponse> result = leaderboardController.getLeaderboards(gameId, "test", pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(leaderboardService).searchLeaderboardsByGame("test", game, pageable);
        verify(leaderboardMapper).toResponse(leaderboards);
    }

    @Test
    void getLeaderboard() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        LeaderboardResponse expected = LeaderboardResponseFixture.aDefaultLeaderboardResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.getLeaderboardById(game, leaderboardId)).thenReturn(leaderboard);
        when(leaderboardMapper.toResponse(leaderboard)).thenReturn(expected);

        LeaderboardResponse result = leaderboardController.getLeaderboard(gameId, leaderboardId);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(leaderboardService).getLeaderboardById(game, leaderboardId);
        verify(leaderboardMapper).toResponse(leaderboard);
    }

    @Test
    void updateLeaderboard() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        LeaderboardRequest request = LeaderboardRequestFixture.aDefaultLeaderboardRequest();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        LeaderboardResponse expected = LeaderboardResponseFixture.aDefaultLeaderboardResponse();

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.updateLeaderboard(game, leaderboardId, request)).thenReturn(leaderboard);
        when(leaderboardMapper.toResponse(leaderboard)).thenReturn(expected);

        LeaderboardResponse result = leaderboardController.updateLeaderboard(gameId, leaderboardId, request);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(leaderboardService).updateLeaderboard(game, leaderboardId, request);
        verify(leaderboardMapper).toResponse(leaderboard);
    }

    @Test
    void getLeaderboardScores() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        Pageable pageable = PageRequest.of(1, 10);
        Page<LeaderboardScore> leaderboardScores = new PageImpl<>(List.of(
                LeaderboardScoreFixture.aDefaultLeaderboardScore(),
                LeaderboardScoreFixture.aDefaultLeaderboardScore()
        ));
        Page<LeaderboardScoreResponse> expected = new PageImpl<>(List.of(
                LeaderboardScoreResponseFixture.aDefaultLeaderboardScoreResponse(),
                LeaderboardScoreResponseFixture.aDefaultLeaderboardScoreResponse()
        ));

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.getLeaderboardById(game, leaderboardId)).thenReturn(leaderboard);
        when(leaderboardScoreService.getScores(leaderboard, pageable)).thenReturn(leaderboardScores);
        when(leaderboardScoreMapper.toResponse(leaderboardScores)).thenReturn(expected);

        PaginatedResponse<LeaderboardScoreResponse> result = leaderboardController.getLeaderboardScores(gameId, leaderboardId, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getById(gameId);
        verify(leaderboardService).getLeaderboardById(game, leaderboardId);
        verify(leaderboardScoreService).getScores(leaderboard, pageable);
        verify(leaderboardScoreMapper).toResponse(leaderboardScores);
    }

    @Test
    void deleteLeaderboard() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(gameService.getById(gameId)).thenReturn(game);

        leaderboardController.deleteLeaderboard(gameId, leaderboardId);

        verify(gameService).getById(gameId);
        verify(leaderboardService).deleteLeaderboard(game, leaderboardId);
    }

    @Test
    void countLeaderboards() {
        UUID gameId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        CountResponse expected = new CountResponse(10L);

        when(gameService.getById(gameId)).thenReturn(game);
        when(leaderboardService.countLeaderboardsByGame(game)).thenReturn(10L);

        CountResponse result = leaderboardController.countLeaderboards(gameId);

        assertEquals(expected, result);
        verify(gameService).getById(gameId);
        verify(leaderboardService).countLeaderboardsByGame(game);
    }
}
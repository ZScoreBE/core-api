package be.zsoft.zscore.core.controller.external.leaderboard;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardScoreMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.ExternalLeaderboardScoreRequest;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardResponseFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardScoreFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardScoreResponseFixture;
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
class ExternalLeaderboardControllerTest {

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
    private ExternalLeaderboardController externalLeaderboardController;

    @Test
    void getLeaderboards() {
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder().id(gameId).build();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Leaderboard> leaderboards = new PageImpl<>(List.of(
                LeaderboardFixture.aDefaultLeaderboard(),
                LeaderboardFixture.aDefaultLeaderboard()
        ));
        Page<LeaderboardResponse> expected = new PageImpl<>(List.of(
                LeaderboardResponseFixture.aDefaultLeaderboardResponse(),
                LeaderboardResponseFixture.aDefaultLeaderboardResponse()
        ));

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(leaderboardService.getLeaderboardsByGame(game,pageable)).thenReturn(leaderboards);
        when(leaderboardMapper.toResponse(leaderboards)).thenReturn(expected);

        PaginatedResponse<LeaderboardResponse> result = externalLeaderboardController.getLeaderboards(pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getAuthenicatedGame();
        verify(leaderboardService).getLeaderboardsByGame(game, pageable);
        verify(leaderboardMapper).toResponse(leaderboards);
    }

    @Test
    void getLeaderboard() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        LeaderboardResponse expected = LeaderboardResponseFixture.aDefaultLeaderboardResponse();

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(leaderboardService.getLeaderboardById(game, leaderboardId)).thenReturn(leaderboard);
        when(leaderboardMapper.toResponse(leaderboard)).thenReturn(expected);

        LeaderboardResponse result = externalLeaderboardController.getLeaderboard(leaderboardId);

        assertEquals(expected, result);
        verify(gameService).getAuthenicatedGame();
        verify(leaderboardService).getLeaderboardById(game, leaderboardId);
        verify(leaderboardMapper).toResponse(leaderboard);
    }

    @Test
    void createLeaderboardScore() {
        UUID gameId = UUID.randomUUID();
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();
        ExternalLeaderboardScoreRequest request = new ExternalLeaderboardScoreRequest(100);
        LeaderboardScore score = LeaderboardScoreFixture.aDefaultLeaderboardScore();
        LeaderboardScoreResponse expected = LeaderboardScoreResponseFixture.aDefaultLeaderboardScoreResponse();

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(leaderboardService.getLeaderboardById(game, leaderboardId)).thenReturn(leaderboard);
        when(leaderboardScoreService.createLeaderboardScore(leaderboard, request)).thenReturn(score);
        when(leaderboardScoreMapper.toResponse(score)).thenReturn(expected);

        LeaderboardScoreResponse result = externalLeaderboardController.createLeaderboardScore(leaderboardId, request);

        assertEquals(expected, result);
        verify(gameService).getAuthenicatedGame();
        verify(leaderboardService).getLeaderboardById(game, leaderboardId);
        verify(leaderboardScoreService).createLeaderboardScore(leaderboard, request);
        verify(leaderboardScoreMapper).toResponse(score);

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

        when(gameService.getAuthenicatedGame()).thenReturn(game);
        when(leaderboardService.getLeaderboardById(game, leaderboardId)).thenReturn(leaderboard);
        when(leaderboardScoreService.getScores(leaderboard, pageable)).thenReturn(leaderboardScores);
        when(leaderboardScoreMapper.toResponse(leaderboardScores)).thenReturn(expected);

        PaginatedResponse<LeaderboardScoreResponse> result = externalLeaderboardController.getLeaderboardScores(leaderboardId, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(gameService).getAuthenicatedGame();
        verify(leaderboardService).getLeaderboardById(game, leaderboardId);
        verify(leaderboardScoreService).getScores(leaderboard, pageable);
        verify(leaderboardScoreMapper).toResponse(leaderboardScores);
    }
}
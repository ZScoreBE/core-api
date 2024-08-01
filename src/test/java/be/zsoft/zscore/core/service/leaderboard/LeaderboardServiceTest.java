package be.zsoft.zscore.core.service.leaderboard;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.leaderboard.LeaderboardMapper;
import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardFixture;
import be.zsoft.zscore.core.fixtures.leaderboard.LeaderboardRequestFixture;
import be.zsoft.zscore.core.repository.leaderboard.LeaderboardRepo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaderboardServiceTest {

    @Mock
    private LeaderboardMapper leaderboardMapper;

    @Mock
    private LeaderboardRepo leaderboardRepo;

    @Mock
    private LeaderboardScoreService leaderboardScoreService;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Test
    void createLeaderboard() {
        Game game = GameFixture.aDefaultGame();
        LeaderboardRequest request = LeaderboardRequestFixture.aDefaultLeaderboardRequest();
        Leaderboard leaderboardFromMapper = LeaderboardFixture.aDefaultLeaderboard();
        Leaderboard expected = LeaderboardFixture.aDefaultLeaderboard();

        when(leaderboardMapper.fromRequest(request)).thenReturn(leaderboardFromMapper);
        when(leaderboardRepo.saveAndFlush(leaderboardFromMapper)).thenReturn(expected);

        Leaderboard result = leaderboardService.createLeaderboard(game, request);

        assertEquals(expected, result);

        verify(leaderboardMapper).fromRequest(request);
        verify(leaderboardRepo).saveAndFlush(leaderboardFromMapper);
    }

    @Test
    void getLeaderboardById_success() {
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard expected = LeaderboardFixture.aDefaultLeaderboard();

        when(leaderboardRepo.findByIdAndGame(leaderboardId, game)).thenReturn(Optional.of(expected));

        Leaderboard result = leaderboardService.getLeaderboardById(game, leaderboardId);

        assertEquals(expected, result);
    }

    @Test
    void getLeaderboardById_notFound() {
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();

        when(leaderboardRepo.findByIdAndGame(leaderboardId, game)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> leaderboardService.getLeaderboardById(game, leaderboardId));
    }

    @Test
    void updateLeaderboard() {
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard expected = LeaderboardFixture.aDefaultLeaderboard();
        LeaderboardRequest request = LeaderboardRequestFixture.aDefaultLeaderboardRequest();

        when(leaderboardRepo.findByIdAndGame(leaderboardId, game)).thenReturn(Optional.of(expected));
        when(leaderboardMapper.fromRequest(request, expected)).thenReturn(expected);
        when(leaderboardRepo.saveAndFlush(expected)).thenReturn(expected);

        Leaderboard result = leaderboardService.updateLeaderboard(game, leaderboardId, request);

        assertEquals(expected, result);

        verify(leaderboardRepo).findByIdAndGame(leaderboardId, game);
        verify(leaderboardMapper).fromRequest(request, expected);
        verify(leaderboardRepo).saveAndFlush(expected);
    }

    @Test
    void deleteLeaderboard() {
        UUID leaderboardId = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        Leaderboard leaderboard = LeaderboardFixture.aDefaultLeaderboard();

        when(leaderboardRepo.findByIdAndGame(leaderboardId, game)).thenReturn(Optional.of(leaderboard));

        leaderboardService.deleteLeaderboard(game, leaderboardId);

        verify(leaderboardRepo).findByIdAndGame(leaderboardId, game);
        verify(leaderboardScoreService).deleteAllScores(leaderboard);
        verify(leaderboardRepo).delete(leaderboard);
    }

    @Test
    void getLeaderboardsByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Leaderboard> expected = new PageImpl<>(List.of(
                LeaderboardFixture.aDefaultLeaderboard(),
                LeaderboardFixture.aDefaultLeaderboard()
        ));

        when(leaderboardRepo.findAllByGame(game, pageable)).thenReturn(expected);

        Page<Leaderboard> result = leaderboardService.getLeaderboardsByGame(game, pageable);

        assertEquals(expected, result);
        verify(leaderboardRepo).findAllByGame(game, pageable);
    }

    @Test
    void searchLeaderboardsByGame() {
        Game game = GameFixture.aDefaultGame();
        Pageable pageable = PageRequest.of(1, 10);
        Page<Leaderboard> expected = new PageImpl<>(List.of(
                LeaderboardFixture.aDefaultLeaderboard(),
                LeaderboardFixture.aDefaultLeaderboard()
        ));

        when(leaderboardRepo.searchOnNameAllByGame("%test%", game, pageable)).thenReturn(expected);

        Page<Leaderboard> result = leaderboardService.searchLeaderboardsByGame("Test", game, pageable);

        assertEquals(expected, result);
        verify(leaderboardRepo).searchOnNameAllByGame("%test%", game, pageable);
    }

    @Test
    void countLeaderboardsByGame() {
        Game game = GameFixture.aDefaultGame();
        long expected = 10;

        when(leaderboardRepo.countByGame(game)).thenReturn(expected);

        long result = leaderboardService.countLeaderboardsByGame(game);

        assertEquals(expected, result);
        verify(leaderboardRepo).countByGame(game);
    }
}
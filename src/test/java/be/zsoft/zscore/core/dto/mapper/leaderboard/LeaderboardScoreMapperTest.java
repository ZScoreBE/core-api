package be.zsoft.zscore.core.dto.mapper.leaderboard;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardScoreResponse;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.entity.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardScoreMapperTest {

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private LeaderboardScoreMapper leaderboardScoreMapper;

    @Test
    void toResponse_single() {
        Player player = Player.builder().id(UUID.randomUUID()).build();
        UUID id1 = UUID.randomUUID();
        LeaderboardScore score1 = LeaderboardScore.builder()
                .id(id1)
                .score(100)
                .player(player)
                .build();

        PlayerResponse playerResponse = new PlayerResponse(UUID.randomUUID(), "player", LocalDateTime.now());
        LeaderboardScoreResponse expected = new LeaderboardScoreResponse(id1, 100, playerResponse);

        when(playerMapper.toResponse(player)).thenReturn(playerResponse);

        LeaderboardScoreResponse result = leaderboardScoreMapper.toResponse(score1);

        assertEquals(expected, result);

        verify(playerMapper).toResponse(player);
    }

    @Test
    void toResponse_page() {
        Player player = Player.builder().id(UUID.randomUUID()).build();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        LeaderboardScore score1 = LeaderboardScore.builder()
                .id(id1)
                .score(100)
                .player(player)
                .build();
        LeaderboardScore score2 = LeaderboardScore.builder()
                .id(id2)
                .score(100)
                .player(player)
                .build();

        PlayerResponse playerResponse = new PlayerResponse(UUID.randomUUID(), "player", LocalDateTime.now());
        Page<LeaderboardScoreResponse> expected = new PageImpl<>(List.of(
                new LeaderboardScoreResponse(id1, 100, playerResponse),
                new LeaderboardScoreResponse(id2, 100, playerResponse)
        ));

        when(playerMapper.toResponse(player)).thenReturn(playerResponse);

        Page<LeaderboardScoreResponse> result = leaderboardScoreMapper.toResponse(new PageImpl<>(List.of(score1, score2)));

        assertEquals(expected, result);

        verify(playerMapper, times(2)).toResponse(player);
    }
}
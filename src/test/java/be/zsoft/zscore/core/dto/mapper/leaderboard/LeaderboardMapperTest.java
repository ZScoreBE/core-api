package be.zsoft.zscore.core.dto.mapper.leaderboard;

import be.zsoft.zscore.core.dto.request.leaderboard.LeaderboardRequest;
import be.zsoft.zscore.core.dto.response.leaderboard.LeaderboardResponse;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScoreType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LeaderboardMapperTest {

    @InjectMocks
    private LeaderboardMapper leaderboardMapper;

    @Test
    void fromRequest() {
        LeaderboardRequest request = new LeaderboardRequest("leaderboard", Sort.Direction.ASC, LeaderboardScoreType.HIGHEST);
        Leaderboard expected = Leaderboard.builder()
                .name("leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .build();

        Leaderboard result = leaderboardMapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Leaderboard leaderboard = Leaderboard.builder()
                .id(id)
                .name("leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .build();
        LeaderboardResponse expected = new LeaderboardResponse(id, "leaderboard", Sort.Direction.ASC, LeaderboardScoreType.HIGHEST);

        LeaderboardResponse result = leaderboardMapper.toResponse(leaderboard);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_page() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Leaderboard leaderboard1 = Leaderboard.builder()
                .id(id1)
                .name("leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .build();
        Leaderboard leaderboard2 = Leaderboard.builder()
                .id(id2)
                .name("leaderboard")
                .direction(Sort.Direction.ASC)
                .scoreType(LeaderboardScoreType.HIGHEST)
                .build();
        Page<Leaderboard> leaderboards = new PageImpl<>(List.of(leaderboard1, leaderboard2));
        Page<LeaderboardResponse> expected = new PageImpl<>(List.of(
                new LeaderboardResponse(id1, "leaderboard", Sort.Direction.ASC, LeaderboardScoreType.HIGHEST),
                new LeaderboardResponse(id2, "leaderboard", Sort.Direction.ASC, LeaderboardScoreType.HIGHEST)
        ));

        Page<LeaderboardResponse> result = leaderboardMapper.toResponse(leaderboards);

        assertEquals(expected, result);
    }
}
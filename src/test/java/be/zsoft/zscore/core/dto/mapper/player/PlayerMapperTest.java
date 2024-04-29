package be.zsoft.zscore.core.dto.mapper.player;

import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PlayerMapperTest {

    @InjectMocks
    private PlayerMapper playerMapper;

    @Test
    void fromRequest() {
        PlayerRequest request = new PlayerRequest("wout");

        Player player = playerMapper.fromRequest(request);

        assertEquals("wout", player.getName());
    }

    @Test
    void fromRequest_update() {
        UUID id = UUID.randomUUID();

        PlayerRequest request = new PlayerRequest("wout updated");
        Player player = Player.builder().id(id).name("wout").build();

        Player result = playerMapper.fromRequest(request, player);

        assertEquals(id, result.getId());
        assertEquals("wout updated", result.getName());
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Player player = Player.builder()
                .id(id)
                .name("wout")
                .lastSignIn(now)
                .build();
        PlayerResponse expected = new PlayerResponse(id, "wout", now);

        PlayerResponse result = playerMapper.toResponse(player);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_list() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Player player1 = Player.builder()
                .id(id1)
                .name("wout")
                .lastSignIn(now)
                .build();
        Player player2 = Player.builder()
                .id(id2)
                .name("wout")
                .lastSignIn(now)
                .build();
        Page<PlayerResponse> expected = new PageImpl<>(
                List.of(
                        new PlayerResponse(id1, "wout", now),
                        new PlayerResponse(id2, "wout", now)
                )
        );

        Page<PlayerResponse> result = playerMapper.toResponse(new PageImpl<>(List.of(player1, player2)));

        assertEquals(expected, result);
    }
}
package be.zsoft.zscore.core.controller.external.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private AchievementProgressService achievementProgressService;

    @InjectMocks
    private ExternalPlayerController externalPlayerController;

    @Test
    void createPlayer() {
        PlayerRequest request = new PlayerRequest("Wout");
        Player player = Player.builder().id(UUID.randomUUID()).build();
        PlayerResponse expected = new PlayerResponse(UUID.randomUUID(), "Wout", LocalDateTime.now());

        when(playerService.createPlayer(request)).thenReturn(player);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.createPlayer(request);

        assertEquals(expected, result);

        verify(playerService).createPlayer(request);
        verify(achievementProgressService).createAchievementProgressesForNewPlayer(player);
        verify(playerMapper).toResponse(player);
    }
}
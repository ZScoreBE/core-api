package be.zsoft.zscore.core.controller.external.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerRequestFixtures;
import be.zsoft.zscore.core.fixtures.player.PlayerResponseFixture;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        PlayerRequest request = PlayerRequestFixtures.aDefaultPlayerRequest();
        Player player = PlayerFixture.aDefaultPlayer();
        PlayerResponse expected = PlayerResponseFixture.aDefaultPlayerResponse();

        when(playerService.createPlayer(request)).thenReturn(player);
        when(playerMapper.toResponse(player)).thenReturn(expected);

        PlayerResponse result = externalPlayerController.createPlayer(request);

        assertEquals(expected, result);

        verify(playerService).createPlayer(request);
        verify(achievementProgressService).createAchievementProgressesForNewPlayer(player);
        verify(playerMapper).toResponse(player);
    }
}
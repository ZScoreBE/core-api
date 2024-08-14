package be.zsoft.zscore.core.dto.mapper.player;

import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerLifeSettingsResponse;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PlayerLifeSettingsMapperTest {

    @InjectMocks
    private PlayerLifeSettingsMapper playerLifeSettingsMapper;

    @Test
    void fromRequest() {
        PlayerLifeSettingsRequest request = new PlayerLifeSettingsRequest(true, 10, 900);
        PlayerLifeSettings expected = PlayerLifeSettings.builder()
                .enabled(true)
                .maxLives(10)
                .giveLifeAfterSeconds(900)
                .build();

        PlayerLifeSettings result = playerLifeSettingsMapper.fromRequest(request, new PlayerLifeSettings());

        assertEquals(expected, result);
    }

    @Test
    void toResponse() {
        UUID id = UUID.randomUUID();
        PlayerLifeSettings settings = PlayerLifeSettings.builder()
                .id(id)
                .enabled(true)
                .maxLives(10)
                .giveLifeAfterSeconds(900)
                .build();
        PlayerLifeSettingsResponse expected = new PlayerLifeSettingsResponse(
                id,
                true,
                10,
                900
        );

        PlayerLifeSettingsResponse result = playerLifeSettingsMapper.toResponse(settings);

        assertEquals(expected, result);
    }
}
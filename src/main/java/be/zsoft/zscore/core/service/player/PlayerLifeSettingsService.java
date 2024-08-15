package be.zsoft.zscore.core.service.player;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.player.PlayerLifeSettingsMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerLifeSettingsRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
import be.zsoft.zscore.core.repository.player.PlayerLifeSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlayerLifeSettingsService {

    private final PlayerLifeSettingsRepo playerLifeSettingsRepo;
    private final PlayerLifeSettingsMapper playerLifeSettingsMapper;

    public Optional<PlayerLifeSettings> getPlayerLifeSettingsAsOptional(Game game) {
        return playerLifeSettingsRepo.findByGame(game);
    }

    public PlayerLifeSettings getPlayerLifeSettings(Game game) {
        return playerLifeSettingsRepo.findByGame(game)
                .orElseThrow(() -> new NotFoundException("Could not find player life settings for game '%s'".formatted(game.getId())));
    }

    public PlayerLifeSettings updatePlayerLifeSettings(PlayerLifeSettingsRequest request, Game game) {
        PlayerLifeSettings settings = playerLifeSettingsRepo.findByGame(game)
                .orElse(new PlayerLifeSettings());

        settings = playerLifeSettingsMapper.fromRequest(request, settings);
        settings.setGame(game);

        return playerLifeSettingsRepo.saveAndFlush(settings);
    }
}

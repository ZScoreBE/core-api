package be.zsoft.zscore.core.dto.mapper.player;

import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.player.Player;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player fromRequest(PlayerRequest request) {
        return fromRequest(request, new Player());
    }

    public Player fromRequest(PlayerRequest request, Player player) {
        player.setName(request.name());
        return player;
    }

    public PlayerResponse toResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getLastSignIn(),
                player.getCurrentLives(),
                player.getLastLifeUpdate()
        );
    }

    public Page<PlayerResponse> toResponse(Page<Player> players) {
        return players.map(this::toResponse);
    }
}

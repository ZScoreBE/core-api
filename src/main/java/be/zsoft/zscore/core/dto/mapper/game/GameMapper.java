package be.zsoft.zscore.core.dto.mapper.game;

import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.dto.response.game.GameResponse;
import be.zsoft.zscore.core.entity.game.Game;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameMapper {

    public Game fromRequest(GameRequest request) {
        return fromRequest(request, new Game());
    }

    public Game fromRequest(GameRequest request, Game game) {
        game.setName(request.name());
        game.setEngine(request.engine());

        return game;
    }

    public GameResponse toResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getName(),
                game.getEngine(),
                game.isActive(),
                game.isSandboxMode(),
                game.getApiKey(),
                game.getGenerationId()
        );
    }

    public List<GameResponse> toResponse(List<Game> games) {
        return games.stream()
                .map(this::toResponse)
                .toList();
    }
}

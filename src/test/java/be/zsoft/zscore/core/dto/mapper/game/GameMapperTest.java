package be.zsoft.zscore.core.dto.mapper.game;

import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.dto.response.game.GameResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.game.GameEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GameMapperTest {

    @InjectMocks
    private GameMapper mapper;

    @Test
    void fromRequest() {
        GameRequest request = new GameRequest("game", GameEngine.UNITY);
        Game expected = Game.builder()
                .name("game")
                .engine(GameEngine.UNITY)
                .build();

        Game result = mapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void fromResponse_single() {
        UUID id = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        Game game = Game.builder()
                .id(id)
                .name("game")
                .engine(GameEngine.UNITY)
                .active(true)
                .sandboxMode(true)
                .apiKey("apiKey")
                .generationId(generationId)
                .build();
        GameResponse expected = new GameResponse(id, "game", GameEngine.UNITY, true, true, "apiKey", generationId);

        GameResponse result = mapper.toResponse(game);

        assertEquals(expected, result);
    }

    @Test
    void fromResponse_list() {
        UUID id = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        List<Game> games = List.of(Game.builder()
                .id(id)
                .name("game")
                .engine(GameEngine.UNITY)
                .active(true)
                .sandboxMode(true)
                .apiKey("apiKey")
                .generationId(generationId)
                .build());
        List<GameResponse> expected = List.of(new GameResponse(id, "game", GameEngine.UNITY, true, true, "apiKey", generationId));

        List<GameResponse> result = mapper.toResponse(games);

        assertEquals(expected, result);
    }
}
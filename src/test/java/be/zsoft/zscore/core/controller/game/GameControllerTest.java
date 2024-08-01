package be.zsoft.zscore.core.controller.game;

import be.zsoft.zscore.core.dto.mapper.game.GameMapper;
import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.dto.response.common.BoolResultResponse;
import be.zsoft.zscore.core.dto.response.game.GameResponse;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.game.GameRequestFixture;
import be.zsoft.zscore.core.fixtures.game.GameResponseFixture;
import be.zsoft.zscore.core.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Test
    void createGame() {
        GameRequest request = GameRequestFixture.aDefaultGameRequest();
        Game game = GameFixture.aDefaultGame();
        GameResponse expected = GameResponseFixture.aDefaultGameResponse();

        when(gameService.createGames(request)).thenReturn(game);
        when(gameMapper.toResponse(game)).thenReturn(expected);

        GameResponse result = gameController.createGame(request);

        assertEquals(expected, result);

        verify(gameService).createGames(request);
        verify(gameMapper).toResponse(game);
    }

    @Test
    void getAllGames() {
        List<Game> games = List.of(
                GameFixture.aDefaultGame(),
                GameFixture.aDefaultGame()
        );
        List<GameResponse> expected = List.of(
                GameResponseFixture.aDefaultGameResponse(),
                GameResponseFixture.aDefaultGameResponse()
        );

        when(gameService.getAllGames()).thenReturn(games);
        when(gameMapper.toResponse(games)).thenReturn(expected);

        List<GameResponse> result = gameController.getAllGames();

        assertEquals(expected, result);

        verify(gameService).getAllGames();
        verify(gameMapper).toResponse(games);
    }

    @Test
    void hasGames() {
        BoolResultResponse expected = new BoolResultResponse(true);

        when(gameService.hasGames()).thenReturn(true);

        BoolResultResponse result = gameController.hasGames();

        assertEquals(expected, result);
        verify(gameService).hasGames();
    }

    @Test
    void getGameById() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        GameResponse expected = GameResponseFixture.aDefaultGameResponse();

        when(gameService.getById(id)).thenReturn(game);
        when(gameMapper.toResponse(game)).thenReturn(expected);

        GameResponse result = gameController.getGame(id);

        assertEquals(expected, result);

        verify(gameService).getById(id);
        verify(gameMapper).toResponse(game);
    }

    @Test
    void regenerateApiKey() {
        UUID id = UUID.randomUUID();
        Game game = GameFixture.aDefaultGame();
        GameResponse expected = GameResponseFixture.aDefaultGameResponse();

        when(gameService.regenerateApiKey(id)).thenReturn(game);
        when(gameMapper.toResponse(game)).thenReturn(expected);

        GameResponse result = gameController.regenerateApiKey(id);

        assertEquals(expected, result);

        verify(gameService).regenerateApiKey(id);
        verify(gameMapper).toResponse(game);
    }
}
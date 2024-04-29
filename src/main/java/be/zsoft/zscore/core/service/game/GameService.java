package be.zsoft.zscore.core.service.game;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.game.GameMapper;
import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.repository.game.GameRepo;
import be.zsoft.zscore.core.security.SecurityUtils;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

    private static final int API_KEY_LENGTH = 64;
    private static final String LIVE_API_KEY_PREFIX = "live_";
    private static final String SANDBOX_API_KEY_PREFIX = "sandbox_";

    private final GameMapper gameMapper;
    private final GameRepo gameRepo;
    private final OrganizationService organizationService;

    /**
     * Create a new game (Sandbox & Live)
     *
     * @param request game to create
     * @return The sandbox game
     */
    public Game createGames(GameRequest request) {
        log.info("Creating sandbox & live games for: {}", request.name());
        Organization organization = organizationService.getMyOrganization();
        UUID generationId = UUID.randomUUID();
        String apiKey = RandomStringUtils.randomAlphanumeric(API_KEY_LENGTH);

        Game sandboxGame = gameMapper.fromRequest(request);
        Game liveGame = gameMapper.fromRequest(request);

        sandboxGame.setActive(true);
        sandboxGame.setSandboxMode(true);
        sandboxGame.setGenerationId(generationId);
        sandboxGame.setApiKey(SANDBOX_API_KEY_PREFIX + apiKey);
        sandboxGame.setOrganization(organization);

        liveGame.setActive(true);
        liveGame.setSandboxMode(false);
        liveGame.setGenerationId(generationId);
        liveGame.setApiKey(LIVE_API_KEY_PREFIX + apiKey);
        liveGame.setOrganization(organization);

        return gameRepo.saveAllAndFlush(List.of(sandboxGame, liveGame)).stream()
                .filter(Game::isSandboxMode)
                .findAny().orElseThrow(() -> new IllegalStateException("We should have a sandbox game by now"));
    }

    public List<Game> getAllGames() {
        Organization organization = organizationService.getMyOrganization();
        return gameRepo.findAllGamesByOrganization(organization);
    }

    public boolean hasGames() {
        Organization organization = organizationService.getMyOrganization();
        return gameRepo.doesOrganizationHaveGames(organization);
    }

    public Game getById(UUID id) {
        return gameRepo.findByIdAndOrganization(id, organizationService.getMyOrganization())
                .orElseThrow(() -> new NotFoundException("No game found with ID: %s".formatted(id)));
    }

    public Game getGameByApiKey(String apiKey) {
        return gameRepo.findByApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException("No game found with API Key: %s".formatted(apiKey)));
    }

    public Game regenerateApiKey(UUID id) {
        List<Game> games = getGameAndRelatedGamesById(id);
        for (Game game : games) {
            String prefix = game.isSandboxMode() ? SANDBOX_API_KEY_PREFIX : LIVE_API_KEY_PREFIX;

            game.setApiKey(prefix + RandomStringUtils.randomAlphanumeric(API_KEY_LENGTH));
        }

        return gameRepo.saveAllAndFlush(games).stream()
                .filter(game -> game.getId().equals(id))
                .findFirst().orElseThrow(() -> new IllegalStateException("We always should have a game with this ID at this point"));
    }

    public Game updateGameGeneralSettings(UUID id, GameRequest request) {
        List<Game> games = getGameAndRelatedGamesById(id).stream()
                .map(game -> gameMapper.fromRequest(request, game))
                .toList();

        return gameRepo.saveAllAndFlush(games).stream()
                .filter(game -> game.getId().equals(id))
                .findFirst().orElseThrow(() -> new IllegalStateException("We always should have a game with this ID at this point"));
    }

    public Game getAuthenicatedGame() {
        return SecurityUtils.getAuthenticatedGame()
                .orElseThrow(() -> new NotFoundException("No game found in the authentication data"));
    }

    private List<Game> getGameAndRelatedGamesById(UUID id) {
        UUID generationId = getById(id).getGenerationId();
        return gameRepo.findAllByGenerationId(generationId);
    }
}

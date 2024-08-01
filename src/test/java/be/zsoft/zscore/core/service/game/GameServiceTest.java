package be.zsoft.zscore.core.service.game;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.game.GameMapper;
import be.zsoft.zscore.core.dto.request.game.GameRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.game.GameRequestFixture;
import be.zsoft.zscore.core.fixtures.organization.OrganizationFixture;
import be.zsoft.zscore.core.repository.game.GameRepo;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameMapper gameMapper;

    @Mock
    private GameRepo gameRepo;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private GameService gameService;

    @Captor
    private ArgumentCaptor<List<Game>> gamesCaptor;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createGames_success() {
        GameRequest request = GameRequestFixture.aDefaultGameRequest();
        Organization organization = OrganizationFixture.aDefaultOrganization();
        Game sandboxGame = GameFixture.aDefaultGame();
        Game liveGame = GameFixture.aDefaultGame();

        Game expextedSandboxGame = GameFixture.aSandboxGame();
        Game expextedLiveGame = GameFixture.aLiveGame();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameMapper.fromRequest(request)).thenReturn(sandboxGame, liveGame);
        when(gameRepo.saveAllAndFlush(anyList())).thenReturn(List.of(expextedSandboxGame, expextedLiveGame));

        Game result = gameService.createGames(request);

        assertEquals(expextedSandboxGame, result);

        verify(organizationService).getMyOrganization();
        verify(gameMapper, times(2)).fromRequest(request);
        verify(gameRepo).saveAllAndFlush(gamesCaptor.capture());

        assertTrue(gamesCaptor.getValue().get(0).isActive());
        assertTrue(gamesCaptor.getValue().get(0).isSandboxMode());
        assertTrue(gamesCaptor.getValue().get(0).getApiKey().startsWith("sandbox_"));
        assertEquals(organization, gamesCaptor.getValue().get(0).getOrganization());
        assertNotNull(gamesCaptor.getValue().get(0).getGenerationId());

        assertTrue(gamesCaptor.getValue().get(1).isActive());
        assertFalse(gamesCaptor.getValue().get(1).isSandboxMode());
        assertTrue(gamesCaptor.getValue().get(1).getApiKey().startsWith("live_"));
        assertEquals(organization, gamesCaptor.getValue().get(1).getOrganization());
        assertNotNull(gamesCaptor.getValue().get(1).getGenerationId());

        assertEquals(gamesCaptor.getValue().get(0).getGenerationId(), gamesCaptor.getValue().get(1).getGenerationId());
    }

    @Test
    void getAllGames() {
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();

        when(organizationService.getMyOrganization()).thenReturn(organization);

        gameService.getAllGames();

        verify(organizationService).getMyOrganization();
        verify(gameRepo).findAllGamesByOrganization(organization);
    }

    @Test
    void hasGames() {
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);

        gameService.hasGames();

        verify(organizationService).getMyOrganization();
        verify(gameRepo).doesOrganizationHaveGames(organization);
    }

    @Test
    void getById_success() {
        UUID id = UUID.randomUUID();
        Game expected = GameFixture.aDefaultGame();
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(id, organization)).thenReturn(Optional.of(expected));

        Game result = gameService.getById(id);

        assertEquals(expected, result);

        verify(gameRepo).findByIdAndOrganization(id, organization);
    }

    @Test
    void getById_notFound() {
        UUID id = UUID.randomUUID();
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(id, organization)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> gameService.getById(id));

        verify(gameRepo).findByIdAndOrganization(id, organization);
    }

    @Test
    void regenerateApiKey_success() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        Game game1 = GameFixture.aLiveGame();
        game1.setGenerationId(generationId);
        Game game2 = GameFixture.aSandboxGame();
        game2.setGenerationId(generationId);
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(game1.getId(), organization)).thenReturn(Optional.of(game1));
        when(gameRepo.findAllByGenerationId(generationId)).thenReturn(List.of(game1, game2));
        when(gameRepo.saveAllAndFlush(anyList())).thenReturn(List.of(game1, game2));

        Game result = gameService.regenerateApiKey(game1.getId());

        verify(gameRepo).findByIdAndOrganization(game1.getId(), organization);
        verify(gameRepo).findAllByGenerationId(generationId);
        verify(gameRepo).saveAllAndFlush(gamesCaptor.capture());

        assertEquals(game1, result);
        assertTrue(gamesCaptor.getValue().get(0).getApiKey().startsWith("live_"));
        assertTrue(gamesCaptor.getValue().get(1).getApiKey().startsWith("sandbox_"));
    }

    @Test
    void regenerateApiKey_IllegalStateException() {
        UUID id1 = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        Game game1 = GameFixture.aLiveGame();
        game1.setGenerationId(generationId);
        Game game2 = GameFixture.aSandboxGame();
        game2.setGenerationId(generationId);
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(id1, organization)).thenReturn(Optional.of(game1));
        when(gameRepo.findAllByGenerationId(generationId)).thenReturn(List.of(game1, game2));
        when(gameRepo.saveAllAndFlush(anyList())).thenReturn(List.of());

        assertThrows(IllegalStateException.class, () -> gameService.regenerateApiKey(id1));

        verify(gameRepo).findByIdAndOrganization(id1, organization);
        verify(gameRepo).findAllByGenerationId(generationId);
        verify(gameRepo).saveAllAndFlush(gamesCaptor.capture());

        assertTrue(gamesCaptor.getValue().get(0).getApiKey().startsWith("live_"));
        assertTrue(gamesCaptor.getValue().get(1).getApiKey().startsWith("sandbox_"));
    }

    @Test
    void updateGameGeneralSettings_success() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        GameRequest request = GameRequestFixture.aDefaultGameRequest();
        Game game1 = GameFixture.aLiveGame();
        game1.setGenerationId(generationId);
        Game game2 = GameFixture.aSandboxGame();
        game2.setGenerationId(generationId);
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(game1.getId(), organization)).thenReturn(Optional.of(game1));
        when(gameRepo.findAllByGenerationId(generationId)).thenReturn(List.of(game1, game2));
        when(gameRepo.saveAllAndFlush(anyList())).thenReturn(List.of(game1, game2));
        when(gameMapper.fromRequest(request, game1)).thenReturn(game1);
        when(gameMapper.fromRequest(request, game2)).thenReturn(game2);

        Game result = gameService.updateGameGeneralSettings(game1.getId(), request);

        verify(gameRepo).findByIdAndOrganization(game1.getId(), organization);
        verify(gameRepo).findAllByGenerationId(generationId);
        verify(gameMapper).fromRequest(request, game1);
        verify(gameMapper).fromRequest(request, game2);
        verify(gameRepo).saveAllAndFlush(List.of(game1, game2));

        assertEquals(game1, result);
    }

    @Test
    void updateGameGeneralSettings_IllegalStateException() {
        UUID id1 = UUID.randomUUID();
        UUID generationId = UUID.randomUUID();
        GameRequest request = GameRequestFixture.aDefaultGameRequest();
        Game game1 = GameFixture.aLiveGame();
        game1.setGenerationId(generationId);
        Game game2 = GameFixture.aSandboxGame();
        game2.setGenerationId(generationId);
        Organization organization = OrganizationFixture.aDefaultOrganization();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(gameRepo.findByIdAndOrganization(id1, organization)).thenReturn(Optional.of(game1));
        when(gameRepo.findAllByGenerationId(generationId)).thenReturn(List.of(game1, game2));
        when(gameRepo.saveAllAndFlush(anyList())).thenReturn(List.of());
        when(gameMapper.fromRequest(request, game1)).thenReturn(game1);
        when(gameMapper.fromRequest(request, game2)).thenReturn(game2);

        assertThrows(IllegalStateException.class, () ->  gameService.updateGameGeneralSettings(id1, request));

        verify(gameRepo).findByIdAndOrganization(id1, organization);
        verify(gameRepo).findAllByGenerationId(generationId);
        verify(gameMapper).fromRequest(request, game1);
        verify(gameMapper).fromRequest(request, game2);
        verify(gameRepo).saveAllAndFlush(List.of(game1, game2));
    }

    @Test
    void getAuthenticatedGame_success() {
        Game expected = GameFixture.aDefaultGame();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, null,expected, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Game result = gameService.getAuthenicatedGame();

        assertEquals(expected, result);
    }

    @Test
    void getAuthenticatedPlayer_noPlayerFound() {
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, null,null, null), "", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(NotFoundException.class, () -> gameService.getAuthenicatedGame());
    }
}
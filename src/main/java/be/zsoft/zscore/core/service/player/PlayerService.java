package be.zsoft.zscore.core.service.player;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.repository.player.PlayerRepo;
import be.zsoft.zscore.core.security.SecurityUtils;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {

    private final PlayerRepo playerRepo;
    private final PlayerMapper playerMapper;
    private final GameService gameService;
    private final Clock clock;

    @Transactional
    public Player createPlayer(PlayerRequest request) {
        Player player = playerMapper.fromRequest(request);
        player.setGame(gameService.getAuthenicatedGame());
        player.setLastSignIn(LocalDateTime.now(clock));

        player = playerRepo.saveAndFlush(player);

        return player;
    }

    public Player getPlayerById(UUID id) {
        return playerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("No player found with id: %s".formatted(id)));
    }

    public Player getPlayerByIdAndGame(UUID id, Game game) {
        return playerRepo.findByIdAndGame(id, game)
                .orElseThrow(() -> new NotFoundException("No player found with id '%s' for game '%s'".formatted(id, game.getId())));
    }

    public Page<Player> getPlayersByGame(Game game, Pageable pageable) {
        return playerRepo.findAllByGame(game, pageable);
    }

    public Page<Player> searchPlayersByGame(String search, Game game, Pageable pageable) {
        return playerRepo.searchAllOnNameByGame("%" + search.toLowerCase() + "%", game, pageable);
    }

    public void rawUpdate(Player player) {
        playerRepo.saveAndFlush(player);
    }

    public Player getAuthenticatedPlayer() {
        return SecurityUtils.getAuthenticatedPlayer()
                .orElseThrow(() -> new NotFoundException("No player found in the authentication data"));
    }


    public long countPlayersByGame(Game game) {
        return playerRepo.countByGame(game);
    }

    public void deletePlayer(Player player) {
        playerRepo.delete(player);
    }

    public List<Player> getAllPlayersByGame(Game game) {
        return playerRepo.findAllByGame(game);
    }
}

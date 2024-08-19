package be.zsoft.zscore.core.controller.external.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
import be.zsoft.zscore.core.service.game.GameService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/players")
public class ExternalPlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final AchievementProgressService achievementProgressService;
    private final CurrencyService currencyService;
    private final WalletService walletService;
    private final GameService gameService;

    @Secured({"ROLE_API"})
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PlayerResponse createPlayer(@RequestBody @Valid PlayerRequest request) {
        Player player = playerService.createPlayer(request);
        achievementProgressService.createAchievementProgressesForNewPlayer(player);

        List<Currency> currencies = currencyService.getAllCurrenciesByGame(gameService.getAuthenicatedGame());
        walletService.createWallets(currencies, player);

        return playerMapper.toResponse(player);
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping("/myself")
    @ResponseBody
    public PlayerResponse getMyself() {
        Player player = playerService.updateAuthenticatedPlayerLivesOnCount();
        return playerMapper.toResponse(player);
    }

    @Secured({"ROLE_PLAYER"})
    @PatchMapping("/myself/take-life")
    @ResponseBody
    @Transactional
    public PlayerResponse takeLife(@RequestParam(name = "amount", required = false, defaultValue = "1") int amount) {
        Player player = playerService.takeLives(amount);
        return playerMapper.toResponse(player);
    }

    @Secured({"ROLE_PLAYER"})
    @PatchMapping("/myself/give-life")
    @ResponseBody
    @Transactional
    public PlayerResponse giveLife(@RequestParam(name = "amount", required = false, defaultValue = "1") int amount) {
        Player player = playerService.giveLives(amount);
        return playerMapper.toResponse(player);
    }
}


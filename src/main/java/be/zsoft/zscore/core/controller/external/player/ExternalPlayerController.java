package be.zsoft.zscore.core.controller.external.player;

import be.zsoft.zscore.core.dto.mapper.player.PlayerMapper;
import be.zsoft.zscore.core.dto.request.player.PlayerRequest;
import be.zsoft.zscore.core.dto.response.player.PlayerResponse;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.service.achievement.AchievementProgressService;
import be.zsoft.zscore.core.service.currency.CurrencyService;
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

    @Secured({"ROLE_API"})
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PlayerResponse createPlayer(@RequestBody @Valid PlayerRequest request) {
        Player player = playerService.createPlayer(request);
        achievementProgressService.createAchievementProgressesForNewPlayer(player);

        List<Currency> currencies = currencyService.getAllCurrenciesByAuthenticatedGame();
        walletService.createWallets(currencies, player);

        return playerMapper.toResponse(player);
    }
}


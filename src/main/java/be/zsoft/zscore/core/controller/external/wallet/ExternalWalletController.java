package be.zsoft.zscore.core.controller.external.wallet;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/external/wallets")
public class ExternalWalletController {

    private final PlayerService playerService;
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Secured({"ROLE_PLAYER"})
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet(@Valid @RequestBody WalletRequest request) {
        Player player = playerService.getAuthenticatedPlayer();
        Wallet wallet = walletService.createWallet(request, player);

        return walletMapper.toResponse(wallet);
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    public PaginatedResponse<WalletResponse> getWallets(Pageable pageable) {
        Player player = playerService.getAuthenticatedPlayer();
        Page<Wallet> wallets = walletService.getWalletsByPlayer(player, pageable);

        return PaginatedResponse.createResponse(walletMapper.toResponse(wallets), "/external/wallets");
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping("/{id}")
    @ResponseBody
    public WalletResponse getWalletById(@PathVariable UUID id) {
        Player player = playerService.getAuthenticatedPlayer();
        Wallet wallet = walletService.getWalletById(id, player);

        return walletMapper.toResponse(wallet);
    }
}

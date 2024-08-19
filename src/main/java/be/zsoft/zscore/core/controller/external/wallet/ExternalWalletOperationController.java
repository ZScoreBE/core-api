package be.zsoft.zscore.core.controller.external.wallet;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.wallet.WalletOperationMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletOperationResponse;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.wallet.WalletOperationService;
import be.zsoft.zscore.core.service.wallet.WalletService;
import jakarta.transaction.Transactional;
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
@RequestMapping("/external/wallets/{walletId}/operations")
public class ExternalWalletOperationController {

    private final PlayerService playerService;
    private final WalletService walletService;
    private final WalletOperationService walletOperationService;
    private final WalletOperationMapper walletOperationMapper;

    @Secured({"ROLE_PLAYER"})
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public WalletOperationResponse createWalletOperation(
            @PathVariable UUID walletId, @Valid @RequestBody WalletOperationRequest request) {
        Player player = playerService.getAuthenticatedPlayer();
        Wallet wallet = walletService.updateWalletAmount(walletId, player, request);

        WalletOperation operation = walletOperationService.createWalletOperation(request, wallet);

        return walletOperationMapper.toResponse(operation);
    }

    @Secured({"ROLE_PLAYER"})
    @GetMapping
    @ResponseBody
    public PaginatedResponse<WalletOperationResponse> getOperations(@PathVariable UUID walletId, Pageable pageable) {
        Player player = playerService.getAuthenticatedPlayer();
        Wallet wallet = walletService.getWalletById(walletId, player);

        Page<WalletOperation> operations = walletOperationService.getWalletOperationsByWallet(wallet, pageable);

        return PaginatedResponse.createResponse(
                walletOperationMapper.toResponse(operations),
                "/external/wallets/%s/operations".formatted(walletId)
        );
    }
}

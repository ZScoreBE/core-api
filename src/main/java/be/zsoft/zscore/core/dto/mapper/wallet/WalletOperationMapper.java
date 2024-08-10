package be.zsoft.zscore.core.dto.mapper.wallet;

import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.dto.response.wallet.WalletOperationResponse;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WalletOperationMapper {

    private final WalletMapper walletMapper;

    public WalletOperation fromRequest(WalletOperationRequest request) {
        return WalletOperation.builder()
                .amount(request.amount())
                .type(request.type())
                .build();
    }

    public WalletOperationResponse toResponse(WalletOperation operation) {
        return new WalletOperationResponse(
                operation.getId(),
                operation.getType(),
                operation.getAmount(),
                walletMapper.toResponse(operation.getWallet())
        );
    }

    public Page<WalletOperationResponse> toResponse(Page<WalletOperation> operations) {
        return operations.map(this::toResponse);
    }
}

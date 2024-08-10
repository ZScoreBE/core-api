package be.zsoft.zscore.core.service.wallet;

import be.zsoft.zscore.core.dto.mapper.wallet.WalletOperationMapper;
import be.zsoft.zscore.core.dto.request.wallet.WalletOperationRequest;
import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import be.zsoft.zscore.core.repository.wallet.WalletOperationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletOperationService {

    private final WalletOperationRepo walletOperationRepo;
    private final WalletOperationMapper walletOperationMapper;

    public WalletOperation createWalletOperation(WalletOperationRequest request, Wallet wallet) {
        WalletOperation walletOperation = walletOperationMapper.fromRequest(request);
        walletOperation.setWallet(wallet);

        return walletOperationRepo.saveAndFlush(walletOperation);
    }

    public Page<WalletOperation> getWalletOperationsByWallet(Wallet wallet, Pageable pageable) {
        return walletOperationRepo.findAllByWallet(wallet, pageable);
    }
}

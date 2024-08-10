package be.zsoft.zscore.core.repository.wallet;

import be.zsoft.zscore.core.entity.wallet.Wallet;
import be.zsoft.zscore.core.entity.wallet.WalletOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletOperationRepo extends JpaRepository<WalletOperation, UUID> {

    @Query("SELECT wo FROM WalletOperation wo WHERE wo.wallet = :wallet")
    Page<WalletOperation> findAllByWallet(Wallet wallet, Pageable pageable);
}

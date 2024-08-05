package be.zsoft.zscore.core.repository.currency;

import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.currency.CurrencyOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyOfferRepo extends JpaRepository<CurrencyOffer, UUID> {

    @Query("SELECT o FROM CurrencyOffer o WHERE o.currency = :currency ORDER BY o.name")
    Page<CurrencyOffer> findAllByCurrency(Currency currency, Pageable pageable);

    @Query("SELECT o FROM CurrencyOffer o WHERE o.currency = :currency AND (LOWER(o.name) LIKE :search OR LOWER(o.key) LIKE :search) ORDER BY o.name")
    Page<CurrencyOffer> searchAllByCurrency(String search, Currency currency, Pageable pageable);

    @Query("SELECT o FROM CurrencyOffer o WHERE o.id = :id AND o.currency = :currency")
    Optional<CurrencyOffer> findByIdAndCurrency(UUID id, Currency currency);
}

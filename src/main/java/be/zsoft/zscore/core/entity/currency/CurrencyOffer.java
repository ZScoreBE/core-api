package be.zsoft.zscore.core.entity.currency;

import be.zsoft.zscore.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "currency_offers")
public class CurrencyOffer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "key_code", length = 5, nullable = false)
    private String key;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "price_ex", nullable = false, precision = 16, scale = 2)
    private BigDecimal priceEx;

    @Column(name = "discount_price_ex", nullable = true, precision = 16, scale = 2)
    private BigDecimal disCountPriceEx;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
}

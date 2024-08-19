package be.zsoft.zscore.core.entity.trigger;

import be.zsoft.zscore.core.entity.BaseEntity;
import be.zsoft.zscore.core.entity.currency.Currency;
import be.zsoft.zscore.core.entity.game.Game;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

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
@Table(name = "triggers")
public class Trigger extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "key_code", length = 5, nullable = false)
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "cost_type", nullable = false)
    private TriggerCostType costType;

    @Column(name = "cost_amount", nullable = true)
    private Integer costAmount;

    @ManyToOne
    @JoinColumn(name = "cost_currency_id", nullable = true)
    private Currency costCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false)
    private TriggerRewardType rewardType;

    @Column(name = "reward_amount", nullable = true)
    private Integer rewardAmount;

    @ManyToOne
    @JoinColumn(name = "reward_currency_id", nullable = true)
    private Currency rewardCurrency;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}

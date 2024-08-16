package be.zsoft.zscore.core.entity.trigger;

import be.zsoft.zscore.core.entity.BaseEntity;
import be.zsoft.zscore.core.entity.game.Game;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.Map;
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

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="trigger_cost_meta_data", joinColumns=@JoinColumn(name="trigger_id"))
    private Map<String, String> costMetaData;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false)
    private TriggerRewardType rewardType;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="trigger_reward_meta_data", joinColumns=@JoinColumn(name="trigger_id"))
    private Map<String, String> rewardMetaData;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}

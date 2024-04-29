package be.zsoft.zscore.core.entity.achievement;

import be.zsoft.zscore.core.entity.BaseEntity;
import be.zsoft.zscore.core.entity.player.Player;
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
@Table(name = "achievement_progress")
public class AchievementProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "current_count", nullable = true)
    private Integer currentCount;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

}

package be.zsoft.zscore.core.entity.player;

import be.zsoft.zscore.core.entity.BaseEntity;
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
@Table(name = "player_life_settings")
public class PlayerLifeSettings extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "max_lives", nullable = false)
    private int maxLives;

    @Column(name = "give_life_after_seconds", nullable = true)
    private Integer giveLifeAfterSeconds;

    @OneToOne
    @JoinColumn(name = "game_id", nullable = false)
    @ToString.Exclude
    private Game game;
}

package be.zsoft.zscore.core.entity.achievement;

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
@Table(name = "achievements")
public class Achievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AchievementType type;

    @Column(name = "needed_count", nullable = true)
    private Integer neededCount;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

}

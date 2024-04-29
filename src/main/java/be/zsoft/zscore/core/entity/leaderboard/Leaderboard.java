package be.zsoft.zscore.core.entity.leaderboard;

import be.zsoft.zscore.core.entity.BaseEntity;
import be.zsoft.zscore.core.entity.game.Game;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.domain.Sort;

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
@Table(name = "leaderboards")
public class Leaderboard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Sort.Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_type", nullable = false)
    private LeaderboardScoreType scoreType;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

}

package be.zsoft.zscore.core.entity.currency;

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
@Table(name = "currencies")
public class Currency extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "key_code", length = 5, nullable = false)
    private String key;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}

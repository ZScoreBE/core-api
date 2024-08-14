package be.zsoft.zscore.core.entity.game;

import be.zsoft.zscore.core.entity.BaseEntity;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.player.PlayerLifeSettings;
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
@Table(name = "games")
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Column(name = "generation_id", nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID generationId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "engine", nullable = false)
    private GameEngine engine;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "sandbox_mode", nullable = false)
    private boolean sandboxMode;

    @Column(name = "api_key", length = 64, unique = true, nullable = false)
    private String apiKey;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @OneToOne(mappedBy = "game")
    private PlayerLifeSettings playerLifeSettings;
}

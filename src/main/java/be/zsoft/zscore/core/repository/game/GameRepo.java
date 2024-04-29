package be.zsoft.zscore.core.repository.game;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepo extends JpaRepository<Game, UUID> {

    @Query("SELECT g FROM Game g WHERE g.organization = :organization ORDER BY g.name ASC")
    List<Game> findAllGamesByOrganization(Organization organization);

    @Query("SELECT count(g) > 0 FROM Game g WHERE g.sandboxMode = true AND g.organization = :organization ORDER BY g.name ASC")
    boolean doesOrganizationHaveGames(Organization organization);

    @Query("SELECT g FROM Game g WHERE g.generationId = :generationId")
    List<Game> findAllByGenerationId(UUID generationId);

    @Query("SELECT g FROM Game g WHERE g.apiKey = :apiKey")
    Optional<Game> findByApiKey(String apiKey);

    @Query("SELECT g FROM Game g WHERE g.id = :id AND g.organization = :organization")
    Optional<Game> findByIdAndOrganization(UUID id, Organization organization);
}

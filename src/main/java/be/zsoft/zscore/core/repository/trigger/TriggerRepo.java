package be.zsoft.zscore.core.repository.trigger;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.trigger.Trigger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TriggerRepo extends JpaRepository<Trigger, UUID> {

    @Query("SELECT t FROM Trigger t WHERE t.game = :game ORDER BY t.name")
    Page<Trigger> findAllByGame(Game game, Pageable pageable);

    @Query("SELECT t FROM Trigger t WHERE t.game = :game AND (LOWER(t.name) LIKE :search OR LOWER(t.key) LIKE :search) ORDER BY t.name")
    Page<Trigger> searchAllByGame(String search, Game game, Pageable pageable);

    @Query("SELECT t FROM Trigger t WHERE t.id = :id AND t.game = :game")
    Optional<Trigger> findByIdAndGame(UUID id, Game game);

}

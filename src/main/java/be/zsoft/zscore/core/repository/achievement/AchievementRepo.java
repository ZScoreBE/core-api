package be.zsoft.zscore.core.repository.achievement;

import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AchievementRepo extends JpaRepository<Achievement, UUID> {

    @Query("SELECT a FROM Achievement a WHERE a.game = :game ORDER BY a.name ASC")
    Page<Achievement> findAllByGame(Game game, Pageable pageable);

    @Query("SELECT a FROM Achievement a WHERE a.game = :game AND LOWER(a.name) LIKE :search ORDER BY a.name ASC")
    Page<Achievement> searchAllOnNameByGame(String search, Game game, Pageable pageable);

    @Query("SELECT a FROM Achievement a WHERE a.id = :id AND a.game = :game")
    Optional<Achievement> findByIdAndGame(UUID id, Game game);

    @Query("SELECT a FROM Achievement a WHERE a.game = :game")
    List<Achievement> findAllByGame(Game game);

    @Query("SELECT COUNT(a) FROM Achievement a WHERE a.game = :game")
    long countByGame(Game game);
}

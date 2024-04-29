package be.zsoft.zscore.core.repository.leaderboard;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardRepo extends JpaRepository<Leaderboard, UUID> {

    @Query("SELECT l FROM Leaderboard l WHERE l.id = :id AND l.game = :game")
    Optional<Leaderboard> findByIdAndGame(UUID id, Game game);

    @Query("SELECT l FROM Leaderboard l WHERE l.game = :game ORDER BY l.name ASC")
    Page<Leaderboard> findAllByGame(Game game, Pageable pageable);

    @Query("SELECT l FROM Leaderboard l WHERE l.game = :game AND LOWER(l.name) LIKE :search ORDER BY l.name ASC")
    Page<Leaderboard> searchOnNameAllByGame(String search, Game game, Pageable pageable);

    @Query("SELECT COUNT(l) FROM Leaderboard l WHERE l.game = :game")
    long countByGame(Game game);
}

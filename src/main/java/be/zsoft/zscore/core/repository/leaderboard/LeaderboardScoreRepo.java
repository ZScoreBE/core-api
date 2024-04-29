package be.zsoft.zscore.core.repository.leaderboard;

import be.zsoft.zscore.core.entity.leaderboard.Leaderboard;
import be.zsoft.zscore.core.entity.leaderboard.LeaderboardScore;
import be.zsoft.zscore.core.entity.player.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderboardScoreRepo extends JpaRepository<LeaderboardScore, UUID> {

    @Query("SELECT ls FROM LeaderboardScore ls WHERE ls.leaderboard = :leaderboard AND ls.player = :player ORDER BY ls.createdAt LIMIT 1")
    Optional<LeaderboardScore> findByLeaderboardAndPlayer(Leaderboard leaderboard, Player player);

    @Query("SELECT ls FROM LeaderboardScore ls WHERE ls.leaderboard = :leaderboard ORDER BY ls.score DESC")
    Page<LeaderboardScore> findAllScoresByLeaderboard(Leaderboard leaderboard, Pageable pageable);

    @Modifying
    @Query("DELETE FROM LeaderboardScore ls WHERE ls.leaderboard = :leaderboard")
    void deleteAllByLeaderboard(Leaderboard leaderboard);

    @Modifying
    @Query("DELETE FROM LeaderboardScore ls WHERE ls.player = :player")
    void deleteAllByPlayer(Player player);
}

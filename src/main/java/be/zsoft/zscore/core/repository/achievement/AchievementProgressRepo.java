package be.zsoft.zscore.core.repository.achievement;

import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
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
public interface AchievementProgressRepo extends JpaRepository<AchievementProgress, UUID> {

    @Query("SELECT ap FROM AchievementProgress ap WHERE ap.achievement = :achievement AND ap.player = :player")
    Optional<AchievementProgress> findByAchievementAndPlayer(Achievement achievement, Player player);

    @Query("SELECT ap FROM AchievementProgress ap WHERE ap.player = :player")
    Page<AchievementProgress> findAllByPlayer(Player player, Pageable pageable);

    @Modifying
    @Query("DELETE FROM AchievementProgress ap WHERE ap.player = :player")
    void deleteAllByPlayer(Player player);

}

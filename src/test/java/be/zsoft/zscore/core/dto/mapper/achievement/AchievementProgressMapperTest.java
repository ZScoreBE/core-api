package be.zsoft.zscore.core.dto.mapper.achievement;

import be.zsoft.zscore.core.dto.response.achievement.AchievementProgressResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
import be.zsoft.zscore.core.entity.achievement.AchievementProgress;
import be.zsoft.zscore.core.entity.achievement.AchievementType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AchievementProgressMapperTest {

    @InjectMocks
    private AchievementProgressMapper achievementProgressMapper;

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        UUID achievementId = UUID.randomUUID();
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(id)
                .currentCount(10)
                .completed(false)
                .achievement(Achievement.builder()
                        .id(achievementId)
                        .name("Achievement 1")
                        .description("Description")
                        .type(AchievementType.MULTIPLE)
                        .neededCount(20)
                        .build())
                .build();
        AchievementProgressResponse expected = new AchievementProgressResponse(
                achievementId,
                "Achievement 1",
                "Description",
                AchievementType.MULTIPLE,
                20,
                10,
                false
        );

        AchievementProgressResponse result = achievementProgressMapper.toResponse(achievementProgress);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id = UUID.randomUUID();
        UUID achievementId = UUID.randomUUID();
        Page<AchievementProgress> achievementProgresses = new PageImpl<>(List.of(
                AchievementProgress.builder()
                .id(id)
                .currentCount(10)
                .completed(false)
                .achievement(Achievement.builder()
                        .id(achievementId)
                        .name("Achievement 1")
                        .description("Description")
                        .type(AchievementType.MULTIPLE)
                        .neededCount(20)
                        .build())
                .build()
        ));
        Page<AchievementProgressResponse> expected = new PageImpl<>(List.of(
                new AchievementProgressResponse(
                achievementId,
                "Achievement 1",
                "Description",
                AchievementType.MULTIPLE,
                20,
                10,
                false
        )
        ));

        Page<AchievementProgressResponse> result = achievementProgressMapper.toResponse(achievementProgresses);

        assertEquals(expected, result);
    }
}
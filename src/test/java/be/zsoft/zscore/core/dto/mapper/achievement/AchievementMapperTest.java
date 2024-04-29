package be.zsoft.zscore.core.dto.mapper.achievement;

import be.zsoft.zscore.core.dto.request.achievement.AchievementRequest;
import be.zsoft.zscore.core.dto.request.achievement.UpdateAchievementRequest;
import be.zsoft.zscore.core.dto.response.achievement.AchievementResponse;
import be.zsoft.zscore.core.entity.achievement.Achievement;
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
class AchievementMapperTest {

    @InjectMocks
    private AchievementMapper mapper;

    @Test
    void fromRequest_create() {
        AchievementRequest request = new AchievementRequest("Achievement 1", "description", AchievementType.MULTIPLE, 50);
        Achievement expected = Achievement.builder()
                .name("Achievement 1")
                .description("description")
                .type(AchievementType.MULTIPLE)
                .neededCount(50)
                .build();

        Achievement result = mapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void fromRequest_update() {
        UpdateAchievementRequest request = new UpdateAchievementRequest("Achievement 1", "description", 50);
        Achievement toUpdate = Achievement.builder()
                .name("Achievement wrong name")
                .description("wrong description ")
                .type(AchievementType.MULTIPLE)
                .neededCount(200)
                .build();
        Achievement expected = Achievement.builder()
                .name("Achievement 1")
                .description("description")
                .type(AchievementType.MULTIPLE)
                .neededCount(50)
                .build();

        Achievement result = mapper.fromRequest(request, toUpdate);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        Achievement achievement = Achievement.builder()
                .id(id)
                .name("Achievement 1")
                .description("description")
                .type(AchievementType.MULTIPLE)
                .neededCount(50)
                .build();
        AchievementResponse expected = new AchievementResponse(id, "Achievement 1", "description", AchievementType.MULTIPLE, 50);

        AchievementResponse result = mapper.toResponse(achievement);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_multiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Achievement achievement1 = Achievement.builder()
                .id(id1)
                .name("Achievement 1")
                .description("description")
                .type(AchievementType.MULTIPLE)
                .neededCount(50)
                .build();
        Achievement achievement2 = Achievement.builder()
                .id(id2)
                .name("Achievement 2")
                .description("description 2")
                .type(AchievementType.MULTIPLE)
                .neededCount(50)
                .build();
        Page<AchievementResponse> expected = new PageImpl<>(List.of(
                new AchievementResponse(id1, "Achievement 1", "description", AchievementType.MULTIPLE, 50)      ,
                new AchievementResponse(id2, "Achievement 2", "description 2", AchievementType.MULTIPLE, 50)
        ));

        Page<AchievementResponse> result = mapper.toResponse(new PageImpl<>(List.of(achievement1, achievement2)));

        assertEquals(expected, result);
    }
}
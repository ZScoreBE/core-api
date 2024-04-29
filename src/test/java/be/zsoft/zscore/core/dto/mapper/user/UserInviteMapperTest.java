package be.zsoft.zscore.core.dto.mapper.user;

import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
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
class UserInviteMapperTest {

    @InjectMocks
    private UserInviteMapper mapper;

    @Test
    void fromRequest() {
        UserInviteRequest request = new UserInviteRequest("wout@z-soft.be", "wout");
        UserInvite expected = UserInvite.builder().email("wout@z-soft.be").name("wout").build();

        UserInvite result = mapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void toResponse() {
        UUID id = UUID.randomUUID();
        UserInvite invite = UserInvite.builder()
                .id(id)
                .email("wout@z-soft.be")
                .name("wout")
                .status(UserInviteStatus.PENDING)
                .build();
        UserInviteResponse expected = new UserInviteResponse(id, "wout@z-soft.be", "wout", UserInviteStatus.PENDING);

        UserInviteResponse result = mapper.toResponse(invite);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_page() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UserInvite invite1 = UserInvite.builder()
                .id(id1)
                .email("wout@z-soft.be")
                .name("wout")
                .status(UserInviteStatus.PENDING)
                .build();
        UserInvite invite2 = UserInvite.builder()
                .id(id2)
                .email("wout@z-soft.be")
                .name("wout")
                .status(UserInviteStatus.PENDING)
                .build();
        UserInviteResponse expected1 = new UserInviteResponse(id1, "wout@z-soft.be", "wout", UserInviteStatus.PENDING);
        UserInviteResponse expected2 = new UserInviteResponse(id2, "wout@z-soft.be", "wout", UserInviteStatus.PENDING);
        Page<UserInviteResponse> expected = new PageImpl<>(List.of(expected1, expected2));

        Page<UserInviteResponse> result = mapper.toResponse(new PageImpl<>(List.of(invite1, invite2)));

        assertEquals(expected, result);
    }
}
package be.zsoft.zscore.core.dto.mapper.user;

import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.dto.response.user.UserResponse;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    void fromRequest() {
        UserRequest request = new UserRequest("wout@z-soft.be", "wout", "pass");

        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        User result = userMapper.fromRequest(request);

        assertEquals("wout@z-soft.be", result.getEmail());
        assertEquals("wout", result.getName());
        assertEquals("encodedPass", result.getPassword());
        assertFalse(result.isActivated());
        assertNotNull(result.getActivationCode());
        assertNotNull(result.getPasswordResetCode());
    }

    @Test
    void fromRequest_update() {
        UpdateUserRequest request = new UpdateUserRequest("new name");
        User user = User.builder().id(UUID.randomUUID()).build();

        User result = userMapper.fromRequest(request, user);

        assertEquals("new name", result.getName());
    }

    @Test
    void toResponse_single() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .name("wout")
                .email("wout@z-soft.be")
                .activated(true)
                .roles(List.of(UserRole.builder().role(Role.ROLE_ORGANIZATION_ADMIN).build()))
                .build();
        UserResponse expected = new UserResponse(id, "wout", "wout@z-soft.be", true, true);

        UserResponse result = userMapper.toResponse(user);

        assertEquals(expected, result);
    }

    @Test
    void toResponse_list() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = User.builder()
                .id(id1)
                .name("wout")
                .email("wout@z-soft.be")
                .activated(true)
                .roles(List.of())
                .build();
        User user2 = User.builder()
                .id(id2)
                .name("wout")
                .email("wout@z-soft.be")
                .activated(true)
                .roles(List.of())
                .build();
        Page<UserResponse> expected = new PageImpl<>(List.of(
                new UserResponse(id1, "wout", "wout@z-soft.be", true, false),
                new UserResponse(id2, "wout", "wout@z-soft.be", true, false)
                ));

        Page<UserResponse> result = userMapper.toResponse(new PageImpl<>(List.of(user1, user2)));

        assertEquals(expected, result);
    }
}
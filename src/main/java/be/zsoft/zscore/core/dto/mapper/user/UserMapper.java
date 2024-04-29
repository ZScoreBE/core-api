package be.zsoft.zscore.core.dto.mapper.user;

import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.dto.response.user.UserResponse;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private static final int RANDOM_CODE_LENGTH = 35;

    private final PasswordEncoder passwordEncoder;

    public User fromRequest(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .activated(false)
                .activationCode(RandomStringUtils.randomAlphanumeric(RANDOM_CODE_LENGTH))
                .passwordResetCode(RandomStringUtils.randomAlphanumeric(RANDOM_CODE_LENGTH))
                .build();
    }

    public User fromRequest(UpdateUserRequest request, User user) {
        user.setName(request.name());

        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isActivated(),
                user.getRoles().stream().anyMatch(role -> role.getRole() == Role.ROLE_ORGANIZATION_ADMIN)
        );
    }

    public Page<UserResponse> toResponse(Page<User> users) {
        return users.map(this::toResponse);
    }
}

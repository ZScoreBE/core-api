package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.repository.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepo userRepo;

    public void validate(UserRequest request) {
        userRepo.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new ApiException(ErrorCodes.USER_EMAIL_NOT_UNIQUE);
                });
    }
}

package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.user.UserMapper;
import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.response.user.UserResponse;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Secured({"ROLE_USER"})
    @GetMapping("/myself")
    @ResponseBody
    public UserResponse getMyself() {
        User user = userService.getMyself();

        return userMapper.toResponse(user);
    }

    @Secured({"ROLE_USER"})
    @PutMapping("/myself")
    @ResponseBody
    public UserResponse updateMyself(@RequestBody @Valid UpdateUserRequest request) {
        User user = userService.updateMyself(request);
        return userMapper.toResponse(user);
    }

    @Secured({"ROLE_ORGANIZATION_ADMIN"})
    @GetMapping("")
    @ResponseBody
    public PaginatedResponse<UserResponse> getUsers(
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Page<User> users = StringUtils.hasText(search) ?
                userService.searchAllUsers(search, pageable) :
                userService.getAllUsers(pageable);
        return PaginatedResponse.createResponse(userMapper.toResponse(users), "/users");
    }

    @PostMapping("/myself/send-reset-password-mail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendResetPasswordMail() {
        userService.sendResetPasswordMail();
    }
}

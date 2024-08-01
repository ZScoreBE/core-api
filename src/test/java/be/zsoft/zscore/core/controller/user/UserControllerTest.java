package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.user.UserMapper;
import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.response.user.UserResponse;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.fixtures.user.UserFixture;
import be.zsoft.zscore.core.fixtures.user.UserRequestFixture;
import be.zsoft.zscore.core.fixtures.user.UserResponseFixture;
import be.zsoft.zscore.core.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void getMyself() {
        User user = UserFixture.aDefaultUser();
        UserResponse expected = UserResponseFixture.aDefaultUserResponse();

        when(userService.getMyself()).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserResponse result = userController.getMyself();

        assertEquals(expected, result);

        verify(userService).getMyself();
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateMyself() {
        UpdateUserRequest request = UserRequestFixture.aDefaultUpdateUserRequest();
        User user = UserFixture.aDefaultUser();
        UserResponse expected = UserResponseFixture.aDefaultUserResponse();

        when(userService.updateMyself(request)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserResponse result = userController.updateMyself(request);

        verify(userService).updateMyself(request);
        verify(userMapper).toResponse(user);

        assertEquals(expected, result);
    }

    @Test
    void getUsers() {
        Page<User> users = new PageImpl<>(List.of(
                UserFixture.aDefaultUser(),
                UserFixture.aDefaultUser()
        ));
        Page<UserResponse> expected = new PageImpl<>(List.of(
                UserResponseFixture.aDefaultUserResponse(),
                UserResponseFixture.aDefaultUserResponse()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(userService.getAllUsers(pageable)).thenReturn(users);
        when(userMapper.toResponse(users)).thenReturn(expected);

        PaginatedResponse<UserResponse> result = userController.getUsers(null, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(userService).getAllUsers(pageable);
        verify(userService, never()).searchAllUsers(anyString(), eq(pageable));
        verify(userMapper).toResponse(users);
    }

    @Test
    void getUsers_search() {
        Page<User> users = new PageImpl<>(List.of(
                UserFixture.aDefaultUser(),
                UserFixture.aDefaultUser()
        ));
        Page<UserResponse> expected = new PageImpl<>(List.of(
                UserResponseFixture.aDefaultUserResponse(),
                UserResponseFixture.aDefaultUserResponse()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(userService.searchAllUsers("test", pageable)).thenReturn(users);
        when(userMapper.toResponse(users)).thenReturn(expected);

        PaginatedResponse<UserResponse> result = userController.getUsers("test", pageable);

        assertEquals(expected.getContent(), result.items());

        verify(userService, never()).getAllUsers(pageable);
        verify(userService).searchAllUsers("test", pageable);
        verify(userMapper).toResponse(users);
    }

    @Test
    void sendResetPasswordMail() {
        userController.sendResetPasswordMail();

        verify(userService).sendResetPasswordMail();
    }
}
package be.zsoft.zscore.core.service.user;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.user.UserMapper;
import be.zsoft.zscore.core.dto.request.action.PasswordResetRequest;
import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.repository.user.UserRepo;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.utils.SMTPEmailService;
import be.zsoft.zscore.core.validation.request.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SMTPEmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void loadUserByUsername_shouldFindUser() {
        User user = User.builder().build();
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.of(user));

        User result = userService.loadUserByUsername("wout@z-soft.be");

        assertEquals(user, result);
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenNotFound() {
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("wout@z-soft.be"));
    }

    @Test
    void getById_shouldFindUser() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).build();

        when(userRepo.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
    }

    @Test
    void getById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void createUser() {
        UserRequest request = new UserRequest("wout@z-soft.be", "wout", "pass");
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).organization(organization).build();
        User userWithoutOrg = User.builder().id(id).build();


        when(userMapper.fromRequest(request)).thenReturn(userWithoutOrg);
        when(userRepo.saveAndFlush(user)).thenReturn(user);

        User result = userService.createUser(request, organization, true);

        assertEquals(user, result);

        verify(userValidator).validate(request);
        verify(userMapper).fromRequest(request);
        verify(userRepo).saveAndFlush(user);
        verify(userRoleService).addRole(user, Role.ROLE_USER);
        verify(userRoleService).addRole(user, Role.ROLE_ORGANIZATION_ADMIN);
        verify(emailService).sendActivationMail(user);
    }

    @Test
    void activateUser_success() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userRepo.findByActivationCode("code")).thenReturn(Optional.of(user));

        userService.activateUser("code");

        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        assertTrue(userArgumentCaptor.getValue().isActivated());
    }

    @Test
    void activateUser_notFound() {
        when(userRepo.findByActivationCode("code")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.activateUser("code"));

        verify(userRepo, never()).saveAndFlush(any(User.class));
    }

    @Test
    void forgotPassword_success() {
        User user = User.builder().id(UUID.randomUUID()).passwordResetCode("resetCode").build();

        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.of(user));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(user);

        userService.forgotPassword("wout@z-soft.be");

        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        verify(emailService).sendPasswordResetMail(user);

        assertNotNull(userArgumentCaptor.getValue().getPasswordResetCode());
        assertNotEquals("resetCode", userArgumentCaptor.getValue().getPasswordResetCode());
    }

    @Test
    void forgotPassword_notFound() {
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.forgotPassword("wout@z-soft.be"));

        verify(userRepo, never()).saveAndFlush(any(User.class));
        verify(emailService, never()).sendPasswordResetMail(any(User.class));
    }

    @Test
    void sendResetPasswordMail() {
        User user = User.builder().id(UUID.randomUUID()).passwordResetCode("resetCode").build();

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, null), "token", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepo.saveAndFlush(any(User.class))).thenReturn(user);

        userService.sendResetPasswordMail();;

        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        verify(emailService).sendPasswordResetMail(user);

        assertNotNull(userArgumentCaptor.getValue().getPasswordResetCode());
        assertNotEquals("resetCode", userArgumentCaptor.getValue().getPasswordResetCode());
    }

    @Test
    void resetPassword_success() {
        User user = User.builder().id(UUID.randomUUID()).build();

        when(userRepo.findByPasswordResetCode("code")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        userService.resetPassword(new PasswordResetRequest("code", "pass"));

        verify(passwordEncoder).encode("pass");
        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        assertEquals("encodedPass", userArgumentCaptor.getValue().getPassword());
    }

    @Test
    void resetPassword_notFound() {
        when(userRepo.findByPasswordResetCode("code")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.resetPassword(new PasswordResetRequest("code", "pass")));

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).saveAndFlush(any(User.class));
    }

    @Test
    void sendActivationMail_success() {
        User user = User.builder().id(UUID.randomUUID()).activationCode("activationCode").build();

        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.of(user));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(user);

        userService.sendActivationMail("wout@z-soft.be");

        verify(userRepo).saveAndFlush(userArgumentCaptor.capture());
        verify(emailService).sendActivationMail(user);

        assertNotNull(userArgumentCaptor.getValue().getActivationCode());
        assertNotEquals("activationCode", userArgumentCaptor.getValue().getActivationCode());
    }

    @Test
    void sendActivationMail_notFound() {
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.sendActivationMail("wout@z-soft.be"));

        verify(userRepo, never()).saveAndFlush(any(User.class));
        verify(emailService, never()).sendActivationMail(any(User.class));
    }

    @Test
    void getMyself_success() {
        User expected = User.builder().id(UUID.randomUUID()).build();

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(expected, null, null, null), "token", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(expected, userService.getMyself());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getMyself_notLoggedIn() {
        SecurityContextHolder.clearContext();
        ApiException ex = assertThrows(ApiException.class, () -> userService.getMyself());

        assertEquals(ErrorCodes.ACCESS_DENIED, ex.getErrorKey());
    }

    @Test
    void updateMyself() {
        UpdateUserRequest request = new UpdateUserRequest("new name");
        User expected = User.builder().id(UUID.randomUUID()).build();

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(expected, null, null, null), "token", null);
        SecurityContextHolder.getContext().setAuthentication(auth);


        when(userMapper.fromRequest(request, expected)).thenReturn(expected);
        when(userRepo.saveAndFlush(expected)).thenReturn(expected);

        assertEquals(expected, userService.updateMyself(request));

        verify(userMapper).fromRequest(request, expected);
        verify(userRepo).saveAndFlush(expected);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getAllUsers() {
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        List<User> users = List.of(
                User.builder().id(UUID.randomUUID()).build(),
                User.builder().id(UUID.randomUUID()).build()
        );
        Page<User> expected = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(1, 10);

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userRepo.findAllByOrganization(organization, pageable)).thenReturn(expected);

        Page<User> result = userService.getAllUsers(pageable);

        assertEquals(expected, result);

        verify(organizationService).getMyOrganization();
        verify(userRepo).findAllByOrganization(organization, pageable);
    }

    @Test
    void searchAllUsers() {
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        List<User> users = List.of(
                User.builder().id(UUID.randomUUID()).build(),
                User.builder().id(UUID.randomUUID()).build()
        );
        Page<User> expected = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(1, 10);

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userRepo.searchAllOnNameAndEmailByOrganization("%test%", organization, pageable)).thenReturn(expected);

        Page<User> result = userService.searchAllUsers("test", pageable);

        assertEquals(expected, result);

        verify(organizationService).getMyOrganization();
        verify(userRepo).searchAllOnNameAndEmailByOrganization("%test%", organization, pageable);
    }
}
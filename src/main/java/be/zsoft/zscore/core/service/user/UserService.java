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
import be.zsoft.zscore.core.security.SecurityUtils;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.utils.SMTPEmailService;
import be.zsoft.zscore.core.validation.request.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final int RANDOM_CODE_LENGTH = 35;

    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final UserRoleService userRoleService;
    private final OrganizationService organizationService;
    private final PasswordEncoder passwordEncoder;
    private final SMTPEmailService smtpEmailService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found for: %s".formatted(username)));
    }

    public User getById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("No user found with id: %s".formatted(id)));
    }

    @Transactional
    public User createUser(UserRequest request, Organization organization, boolean admin) {
        userValidator.validate(request);
        User user = userMapper.fromRequest(request);
        user.setOrganization(organization);

        user = userRepo.saveAndFlush(user);

        userRoleService.addRole(user, Role.ROLE_USER);

        if (admin) {
            userRoleService.addRole(user, Role.ROLE_ORGANIZATION_ADMIN);
        }

        smtpEmailService.sendActivationMail(user);

        return user;
    }

    public void activateUser(String code) {
        User user = userRepo.findByActivationCode(code)
                .orElseThrow(() -> new NotFoundException("Could not find any user with activation code: %s".formatted(code)));

        user.setActivated(true);
        userRepo.saveAndFlush(user);
    }

    public void forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No user found for: %s".formatted(email)));

        forgotPassword(user);
    }

    public void sendResetPasswordMail() {
        forgotPassword(getMyself());
    }

    public void resetPassword(PasswordResetRequest request) {
        User user = userRepo.findByPasswordResetCode(request.code())
                .orElseThrow(() -> new NotFoundException("Could not find any user with activation code: %s".formatted(request.code())));

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepo.saveAndFlush(user);
    }

    public void sendActivationMail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No user found for: %s".formatted(email)));

        user.setActivationCode(RandomStringUtils.randomAlphanumeric(RANDOM_CODE_LENGTH));
        user = userRepo.saveAndFlush(user);

        smtpEmailService.sendActivationMail(user);
    }

    public User getMyself() {
        return SecurityUtils.getAuthenticatedUser()
                .orElseThrow(() -> new ApiException(ErrorCodes.ACCESS_DENIED));
    }

    public User updateMyself(UpdateUserRequest request) {
        User user = userMapper.fromRequest(request, getMyself());
        return userRepo.saveAndFlush(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        Organization organization = organizationService.getMyOrganization();
        return userRepo.findAllByOrganization(organization, pageable);
    }

    public Page<User> searchAllUsers(String search, Pageable pageable) {
        Organization organization = organizationService.getMyOrganization();
        return userRepo.searchAllOnNameAndEmailByOrganization("%" + search.toLowerCase() + "%", organization, pageable);
    }

    private void forgotPassword(User user) {
        user.setPasswordResetCode(RandomStringUtils.randomAlphanumeric(RANDOM_CODE_LENGTH));
        user = userRepo.saveAndFlush(user);

        smtpEmailService.sendPasswordResetMail(user);
    }
}

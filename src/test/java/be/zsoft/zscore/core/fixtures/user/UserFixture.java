package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserRole;
import be.zsoft.zscore.core.fixtures.organization.OrganizationFixture;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserFixture {

    public static User aDefaultUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Wout")
                .email("wout@zsoft.be")
                .password("pass")
                .organization(OrganizationFixture.aDefaultOrganization())
                .activated(true)
                .activationCode("abc")
                .passwordResetCode("123")
                .roles(List.of(UserRole.builder().role(Role.ROLE_USER).build()))
                .build();
    }

    public static User aNotActivatedUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Wout")
                .email("wout@zsoft.be")
                .password("pass")
                .organization(OrganizationFixture.aDefaultOrganization())
                .activated(false)
                .activationCode("abc")
                .passwordResetCode("123")
                .roles(List.of(UserRole.builder().role(Role.ROLE_USER).build()))
                .build();
    }

    public static User aUserWithoutOrganization() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("Wout")
                .email("wout@zsoft.be")
                .password("pass")
                .activated(true)
                .activationCode("abc")
                .passwordResetCode("123")
                .roles(List.of(UserRole.builder().role(Role.ROLE_USER).build()))
                .build();
    }
}

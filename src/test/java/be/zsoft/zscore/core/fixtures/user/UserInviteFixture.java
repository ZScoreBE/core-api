package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.fixtures.organization.OrganizationFixture;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UserInviteFixture {

    public static UserInvite aDefaultInvite() {
        return UserInvite.builder()
                .id(UUID.randomUUID())
                .organization(OrganizationFixture.aDefaultOrganization())
                .build();
    }
}

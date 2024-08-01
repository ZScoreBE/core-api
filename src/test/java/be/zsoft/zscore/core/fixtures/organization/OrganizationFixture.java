package be.zsoft.zscore.core.fixtures.organization;

import be.zsoft.zscore.core.entity.organization.Organization;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class OrganizationFixture {

    public static Organization aDefaultOrganization() {
        return Organization.builder()
                .id(UUID.randomUUID())
                .name("org")
                .build();
    }
}

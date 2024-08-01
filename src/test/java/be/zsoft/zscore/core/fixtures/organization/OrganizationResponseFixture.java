package be.zsoft.zscore.core.fixtures.organization;

import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class OrganizationResponseFixture {

    public static OrganizationResponse aDefaultOrganizationResponse() {
        return new OrganizationResponse(UUID.randomUUID(), "org");
    }
}

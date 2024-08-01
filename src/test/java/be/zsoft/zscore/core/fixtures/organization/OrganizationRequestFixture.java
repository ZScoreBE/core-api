package be.zsoft.zscore.core.fixtures.organization;

import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrganizationRequestFixture {

    public static OrganizationRequest aDefaultOrganizationRequest() {
        return new OrganizationRequest("org");
    }
}

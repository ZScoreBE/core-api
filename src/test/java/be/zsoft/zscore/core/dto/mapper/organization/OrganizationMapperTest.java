package be.zsoft.zscore.core.dto.mapper.organization;

import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrganizationMapperTest {

    @InjectMocks
    private OrganizationMapper mapper;

    @Test
    void fromRequest() {
        OrganizationRequest request = new OrganizationRequest("org");
        Organization expected = Organization.builder()
                .name("org")
                .build();

        Organization result = mapper.fromRequest(request);

        assertEquals(expected, result);
    }

    @Test
    void toResponse() {
        UUID id = UUID.randomUUID();
        OrganizationResponse expected = new OrganizationResponse(id,"org");
        Organization organization  = Organization.builder().id(id).name("org").build();

        OrganizationResponse result = mapper.toResponse(organization);

        assertEquals(expected, result);
    }
}
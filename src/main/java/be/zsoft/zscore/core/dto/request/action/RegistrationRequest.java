package be.zsoft.zscore.core.dto.request.action;

import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrationRequest(
        OrganizationRequest organization,
        @NotNull UserRequest user,
        UUID inviteCode
) {
}

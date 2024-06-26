package be.zsoft.zscore.core.security.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TokenForPlayerRequest(
        @NotNull UUID id
) {
}

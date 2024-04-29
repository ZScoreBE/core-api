package be.zsoft.zscore.core.security.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}

package be.zsoft.zscore.core.common;

public record ApiError(
        int status,
        String errorKey,
        Object detail
) {
}

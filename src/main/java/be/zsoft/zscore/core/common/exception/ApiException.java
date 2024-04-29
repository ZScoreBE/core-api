package be.zsoft.zscore.core.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final String errorKey;
    private final String detailMessage;

    public ApiException(String errorKey) {
        this(errorKey, null);
    }

    public ApiException(String errorKey, String message) {
        super(errorKey);
        this.errorKey = errorKey;
        detailMessage = message;
    }

}
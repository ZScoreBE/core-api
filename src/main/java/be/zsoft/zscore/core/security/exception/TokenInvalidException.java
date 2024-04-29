package be.zsoft.zscore.core.security.exception;

import org.springframework.security.access.AccessDeniedException;

public class TokenInvalidException extends AccessDeniedException {

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

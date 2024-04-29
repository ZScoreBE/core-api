package be.zsoft.zscore.core.security.exception;

import org.springframework.security.access.AccessDeniedException;

public class TokenExpiredException extends AccessDeniedException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}

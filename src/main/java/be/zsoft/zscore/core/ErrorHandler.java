package be.zsoft.zscore.core;

import be.zsoft.zscore.core.common.ApiError;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.security.exception.TokenExpiredException;
import be.zsoft.zscore.core.security.exception.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleValidationError(MethodArgumentNotValidException ex) {
        var result = ex.getBindingResult();
        var errors = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ApiError(HttpStatus.BAD_REQUEST.value(), ErrorCodes.VALIDATION_ERROR, errors);
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleNotFound(NotFoundException ex) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), ErrorCodes.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleApiException(ApiException ex) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getErrorKey(), ex.getDetailMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiError handleAccessDenied(RuntimeException ex) {
        if (ex instanceof TokenExpiredException) {
            return new ApiError(HttpStatus.UNAUTHORIZED.value(), ErrorCodes.TOKEN_EXPIRED, ex.getMessage());
        }

        if (ex instanceof TokenInvalidException) {
            return new ApiError(HttpStatus.UNAUTHORIZED.value(), ErrorCodes.INVALID_TOKEN, ex.getMessage());
        }

        return new ApiError(HttpStatus.UNAUTHORIZED.value(), ErrorCodes.ACCESS_DENIED, ex.getMessage());
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError handleAllOtherExceptions(Exception ex) {
        log.error("Internal error!", ex);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCodes.INTERNAL_ERROR, ex.getMessage());
    }

}

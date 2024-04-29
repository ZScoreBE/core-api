package be.zsoft.zscore.core;

public record ErrorCodes() {
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String NOT_ACTIVATED = "NOT_ACTIVATED";
    public static final String USER_EMAIL_NOT_UNIQUE = "USER_EMAIL_NOT_UNIQUE";
    public static final String ALREADY_INVITED = "ALREADY_INVITED";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String ORGANIZATION_NAME_IS_REQUIRED = "ORGANIZATION_NAME_IS_REQUIRED";
    public static final String ACHIEVEMENT_COUNT_INCREASE_SHOULD_HAVE_TYPE_MULTIPLE = "ACHIEVEMENT_COUNT_INCREASE_SHOULD_HAVE_TYPE_MULTIPLE";
    public static final String ACHIEVEMENT_COUNT_DECREASE_SHOULD_HAVE_TYPE_MULTIPLE = "ACHIEVEMENT_COUNT_DECREASE_SHOULD_HAVE_TYPE_MULTIPLE";
    public static final String ACHIEVEMENT_ALREADY_COMPLETED = "ACHIEVEMENT_ALREADY_COMPLETED";
}

package com.boyouquan.enumration;

public enum ErrorCode implements ErrorMessage {

    LOGIN_USERNAME_INVALID("login_username_invalid", "Login Username Invalid"),
    LOGIN_PASSWORD_INVALID("login_password_invalid", "Login Password Invalid"),
    LOGIN_USERNAME_PASSWORD_INVALID("login_username_password_invalid", "Login Username Password Invalid"),
    INTERNAL_SERVER_ERROR("internal_server_error", "Internal Server Error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

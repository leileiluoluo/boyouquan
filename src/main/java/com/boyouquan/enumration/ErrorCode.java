package com.boyouquan.enumration;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements RestErrorResponse {

    LOGIN_USERNAME_INVALID(HttpStatus.BAD_REQUEST, "login_username_invalid", "账号无效"),
    LOGIN_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_password_invalid", "密码无效"),
    LOGIN_USERNAME_PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "login_username_password_invalid", "账号密码无效"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error", "服务器内部错误");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
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

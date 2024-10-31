package com.boyouquan.enumration;

import org.springframework.http.HttpStatus;

public interface RestErrorResponse {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();

}

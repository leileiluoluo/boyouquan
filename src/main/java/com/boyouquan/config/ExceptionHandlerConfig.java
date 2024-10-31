package com.boyouquan.config;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.model.ErrorResponse;
import com.boyouquan.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseUtil.errorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}

package com.boyouquan.util;

import com.boyouquan.enumration.ErrorMessage;
import com.boyouquan.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<ErrorResponse> errorResponse(ErrorMessage errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(errorMessage.getCode());
        errorResponse.setMessage(errorMessage.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}

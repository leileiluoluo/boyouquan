package com.boyouquan.util;

import org.apache.commons.validator.routines.EmailValidator;

public class EmailUtil {

    public static boolean isEmailValid(String emailAddress) {
        return EmailValidator.getInstance().isValid(emailAddress);
    }

}

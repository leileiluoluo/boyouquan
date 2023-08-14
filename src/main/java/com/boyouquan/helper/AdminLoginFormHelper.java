package com.boyouquan.helper;

import com.boyouquan.model.AdminLoginForm;
import com.boyouquan.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AdminLoginFormHelper {

    @Autowired
    private UserService userService;

    public void paramsValidation(AdminLoginForm adminLoginForm, Errors errors) {
        // name
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            errors.rejectValue("username", "fields.invalid", "账号不能为空");
            return;
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            errors.rejectValue("password", "fields.invalid", "密码不能为空");
            return;
        }

        // check user
        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            errors.rejectValue("username", "fields.invalid", "账号或密码无效！");
            errors.rejectValue("password", "fields.invalid", "账号或密码无效！");
        }
    }

}

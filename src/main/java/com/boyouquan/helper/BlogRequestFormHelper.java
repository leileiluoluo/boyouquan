package com.boyouquan.helper;

import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BlogRequestFormHelper {

    @Autowired
    private BlogRequestService blogRequestService;

    public void paramsValidation(BlogRequestForm blogRequestForm, Errors errors) {
        // name
        if (StringUtils.isBlank(blogRequestForm.getName())) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能为空");
        } else if (blogRequestForm.getName().length() > 20) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能大于20个字");
        }

        // description
        if (StringUtils.isBlank(blogRequestForm.getDescription())) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能为空");
        } else if (blogRequestForm.getDescription().length() < 10) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能少于10个字");
        }

        // rss address
        if (StringUtils.isBlank(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS地址不能为空");
        } else if (!blogRequestForm.getRssAddress().startsWith("http")) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS地址不正确");
        }

        // email
        if (StringUtils.isBlank(blogRequestForm.getAdminEmail())) {
            errors.rejectValue("adminEmail", "fields.invalid", "博主邮箱不能为空");
        } else if (!EmailUtil.isEmailValid(blogRequestForm.getAdminEmail())) {
            errors.rejectValue("adminEmail", "fields.invalid", "邮箱格式不正确");
        }

        // exists?
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "您要提交的博客已存在！");
        }
    }

}

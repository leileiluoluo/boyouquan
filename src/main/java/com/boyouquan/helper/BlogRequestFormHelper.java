package com.boyouquan.helper;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.BlogRequestForm;
import com.boyouquan.service.BlogRequestService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Component
public class BlogRequestFormHelper {

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private BlogRequestService blogRequestService;

    public void paramsValidation(BlogRequestForm blogRequestForm, Errors errors) {
        // name
        if (StringUtils.isBlank(blogRequestForm.getName())) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能为空");
            return;
        }
        String name = blogRequestForm.getName().trim();
        if (name.length() > 20) {
            errors.rejectValue("name", "fields.invalid", "博客名称不能大于20个字");
            return;
        }

        // description
        if (StringUtils.isBlank(blogRequestForm.getDescription())) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能为空");
            return;
        }
        String description = blogRequestForm.getDescription().trim();
        if (description.length() < 10) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能少于10个字");
            return;
        }
        if (description.length() > 300) {
            errors.rejectValue("description", "fields.invalid", "博客描述不能大于300个字");
            return;
        }

        // rss address
        if (StringUtils.isBlank(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS 地址不能为空");
            return;
        }
        String rssAddress = blogRequestForm.getRssAddress().trim();
        if (!rssAddress.startsWith("http")) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS 地址不正确");
            return;
        }
        if (!CommonUtils.getDomain(rssAddress).contains("/")) {
            errors.rejectValue("rssAddress", "fields.invalid", "RSS 地址不正确");
            return;
        }

        // refusing domains check
        String domain = CommonUtils.getDomainFromURL(rssAddress);
        if (boYouQuanConfig.getDomainsRefuseToJoin().stream().anyMatch(domain::endsWith)) {
            errors.rejectValue("rssAddress", "fields.invalid", "该域名拒绝加入");
            return;
        }

        // email
        if (StringUtils.isBlank(blogRequestForm.getAdminEmail())) {
            errors.rejectValue("adminEmail", "fields.invalid", "博主邮箱不能为空");
            return;
        }
        String adminEmail = blogRequestForm.getAdminEmail().trim();
        if (!EmailUtil.isEmailValid(adminEmail)) {
            errors.rejectValue("adminEmail", "fields.invalid", "邮箱格式不正确");
            return;
        }

        // exists?
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            errors.rejectValue("rssAddress", "fields.invalid", "您要提交的博客已存在！");
        }
    }

    public Map<String, String> paramsValidation(BlogRequestForm blogRequestForm) {
        Map<String, String> error = new HashMap<>();
        // name
        if (StringUtils.isBlank(blogRequestForm.getName())) {
            error.put("name", "博客名称不能为空");
            return error;
        }
        String name = blogRequestForm.getName().trim();
        if (name.length() > 20) {
            error.put("name", "博客名称不能大于20个字");
            return error;
        }

        // description
        if (StringUtils.isBlank(blogRequestForm.getDescription())) {
            error.put("description", "博客描述不能为空");
            return error;
        }
        String description = blogRequestForm.getDescription().trim();
        if (description.length() < 10) {
            error.put("description", "博客描述不能少于10个字");
            return error;
        }
        if (description.length() > 300) {
            error.put("description", "博客描述不能大于300个字");
            return error;
        }

        // rss address
        if (StringUtils.isBlank(blogRequestForm.getRssAddress())) {
            error.put("rssAddress", "RSS 地址不能为空");
            return error;
        }
        String rssAddress = blogRequestForm.getRssAddress().trim();
        if (!rssAddress.startsWith("http")) {
            error.put("rssAddress", "RSS 地址不正确");
            return error;
        }
        if (!CommonUtils.getDomain(rssAddress).contains("/")) {
            error.put("rssAddress", "RSS 地址不正确");
            return error;
        }

        // refusing domains check
        String domain = CommonUtils.getDomainFromURL(rssAddress);
        if (boYouQuanConfig.getDomainsRefuseToJoin().stream().anyMatch(domain::endsWith)) {
            error.put("rssAddress", "该域名拒绝加入");
            return error;
        }

        // email
        if (StringUtils.isBlank(blogRequestForm.getAdminEmail())) {
            error.put("adminEmail", "博主邮箱不能为空");
            return error;
        }
        String adminEmail = blogRequestForm.getAdminEmail().trim();
        if (!EmailUtil.isEmailValid(adminEmail)) {
            error.put("adminEmail", "邮箱格式不正确");
            return error;
        }

        // exists?
        if (null != blogRequestService.getByRssAddress(blogRequestForm.getRssAddress())) {
            error.put("rssAddress", "您要提交的博客已存在！");
            return error;
        }

        return null;
    }

}

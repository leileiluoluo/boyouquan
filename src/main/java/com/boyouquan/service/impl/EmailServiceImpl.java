package com.boyouquan.service.impl;

import com.boyouquan.config.BoYouQuanConfig;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogRequest;
import com.boyouquan.model.BlogRequestInfo;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private BoYouQuanConfig boYouQuanConfig;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private BlogService blogService;

    @Override
    public void sendBlogRequestSubmittedNotice(BlogRequest blogRequest) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String siteManagerEmail = "contact@boyouquan.com";
            String subject = String.format("[博友圈] 新提交的博客「%s」需要审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);
            Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_add", context);

            // send
            send(siteManagerEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogRequestApprovedNotice(BlogRequest blogRequest) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 恭喜您！您的博客「%s」已通过审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);
            Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_approved", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogSystemCollectedNotice(BlogRequest blogRequest) {
        if (!boYouQuanConfig.getEmailEnabled()) {
            return;
        }

        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 恭喜您！您的博客「%s」已被博友圈收录！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);
            Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_collected", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void sendBlogRequestRejectNotice(BlogRequest blogRequest) {
        if (null != blogRequest) {
            String adminEmail = blogRequest.getAdminEmail();
            String subject = String.format("[博友圈] 很抱歉！您的博客「%s」未通过审核！", blogRequest.getName());

            Context context = new Context();
            BlogRequestInfo blogRequestInfo = new BlogRequestInfo();
            BeanUtils.copyProperties(blogRequest, blogRequestInfo);
            Blog blog = blogService.getByRSSAddress(blogRequest.getRssAddress());
            if (null != blog) {
                blogRequestInfo.setDomainName(blog.getDomainName());
            }

            context.setVariable("blogRequestInfo", blogRequestInfo);

            String text = templateEngine.process("email/blog_request_template_rejected", context);

            // send
            send(adminEmail, subject, text, true);
        }
    }

    @Override
    public void send(String to, String subject, String content, boolean html) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, html);
            message.setFrom("contact@boyouquan.com");

            // send
            javaMailSender.send(message);

            logger.info("email successfully sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("email sent failed!", e);
        }
    }

}

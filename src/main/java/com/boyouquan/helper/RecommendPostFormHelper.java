package com.boyouquan.helper;

import com.boyouquan.model.Post;
import com.boyouquan.model.RecommendPostForm;
import com.boyouquan.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RecommendPostFormHelper {

    @Autowired
    private PostService postService;

    public void paramsValidation(RecommendPostForm recommendPostForm, Errors errors) {
        // name
        if (StringUtils.isBlank(recommendPostForm.getLink())) {
            errors.rejectValue("link", "fields.invalid", "URL 不能为空");
            return;
        }

        // exists?
        Post post = postService.getByLink(recommendPostForm.getLink());
        if (null == post) {
            errors.rejectValue("link", "fields.invalid", "URL 未收录");
            return;
        }
        if (post.getRecommended()) {
            errors.rejectValue("link", "fields.invalid", "URL 已推荐 ");
        }
    }

}

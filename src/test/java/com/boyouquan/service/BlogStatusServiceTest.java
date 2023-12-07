package com.boyouquan.service;

import com.boyouquan.model.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BlogStatusServiceTest {

    @Autowired
    private BlogStatusService blogStatusService;

    @Test
    public void testDetectBlogStatus() {
        Blog blog = new Blog();
        blog.setDomainName("www.crant.cn");
        blog.setAddress("http://www.crant.cn/");
        blogStatusService.detectBlogStatus(blog);
    }

}

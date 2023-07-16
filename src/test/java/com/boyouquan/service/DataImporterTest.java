//package com.boyouquan.service;
//
//import com.boyouquan.enumeration.BlogEnums;
//import com.boyouquan.model.Blog;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Date;
//
//@SpringBootTest
//public class DataImporterTest {
//
//    @Autowired
//    private BlogService blogService;
//
//    @Test
//    public void testLoadData() {
//        for (BlogEnums blogEnum : BlogEnums.values()) {
//            String adminEmail = blogEnum.getEmail();
//            String feedAddress = blogEnum.getFeedAddress();
//            String collectedAt = blogEnum.getCreatedAt();
//            String description = blogEnum.getDescription();
//            Boolean selfSubmitted = blogEnum.getSelfSubmitted();
//
//            // save
//            Blog blog = new Blog();
//            blog.setDomainName("");
//            blog.setAdminEmail(adminEmail);
//            blog.setName("");
//            blog.setAddress("");
//            blog.setRssAddress(feedAddress);
//            blog.setDescription(description);
//            blog.setSelfSubmitted(selfSubmitted);
//            blog.setCollectedAt();
//            blog.setUpdatedAt(new Date());
//
//            blogService.save();
//        }
//    }
//
//}

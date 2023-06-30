//package com.boyouquan;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//import com.boyouquan.controller.HomeController;
//import com.boyouquan.service.BlogPostService;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(controllers = HomeController.class)
//public class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BlogPostService blogPostService;
//
//    @Test
//    public void testIndex() throws Exception {
//        mockMvc.perform(get("/home"))
//                .andExpect(content().string(containsString("博友圈")));
//    }
//
//}

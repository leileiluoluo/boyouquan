package com.boyouquan.controller;

import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("planet-shuttle")
public class PlanetShuttleController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String shuttle(Model model) {
        Blog blog = blogService.getByRandom();

        model.addAttribute("blogName", blog.getName());
        model.addAttribute("blogAddress", blog.getAddress());

        return "planet_shuttle/planet_shuttle";
    }

}

package com.boyouquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/similar-sites")
public class SimilarSitesController {

    @GetMapping("")
    public String similarSites(Model model) {
        return "similar_sites/similar_sites";
    }

}

package com.boyouquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sponsor")
public class SponsorController {

    @GetMapping("")
    public String about() {
        return "sponsor/sponsor";
    }

}

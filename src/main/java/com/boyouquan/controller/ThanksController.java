package com.boyouquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thanks")
public class ThanksController {

    @GetMapping("")
    public String thanks() {
        return "thanks/thanks";
    }

}

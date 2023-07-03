package com.boyouquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/release-notes")
public class ReleaseController {

    @GetMapping("")
    public String releaseNotes(Model model) {
        return "release_notes/release_notes";
    }

    @GetMapping("/{version}")
    public String releaseNotesWithVersion(@PathVariable("version") String version, Model model) {
        return String.format("%s/%s/index", "release_notes", version);
    }

}

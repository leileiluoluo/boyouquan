package com.boyouquan.controller;

import com.boyouquan.service.GravatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/gravatar")
public class GravatarController {

    @Autowired
    private GravatarService gravatarService;

    @GetMapping("/{md5Email}")
    public ResponseEntity<byte[]> image(@PathVariable("md5Email") String md5Email, @RequestParam("size") int size) {
        byte[] bytes = gravatarService.getImage(md5Email, size);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}

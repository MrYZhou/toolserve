package com.lar.system.file;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gstorage")
public class GoogleStorage {
    // 获取授权码
    @GetMapping("/redirectUri")
    public String redirectUri(String code) {
        System.out.println(code);
        return code;
    }
}

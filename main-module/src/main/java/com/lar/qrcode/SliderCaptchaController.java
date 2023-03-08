package com.lar.qrcode;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/captcha")
public class SliderCaptchaController {

    @PostMapping("/verify")
    public boolean isVerify(List<Integer> datas) {
        int sum = 0;
        for (Integer data : datas) {
            sum += data;
        }
        double avg = sum * 1.0 / datas.size();

        double sum2 = 0.0;
        for (Integer data : datas) {
            sum2 += Math.pow(data - avg, 2);
        }

        double stddev = sum2 / datas.size();
        return stddev != 0;
    }

}

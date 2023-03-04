package com.lar.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailTask {
    @Autowired
    MailSender mailSender;
    @Autowired
    SimpleMailMessage simpleMailMessage;

//    @Autowired
//    MimeMessageHelper mimeMessageHelper;
//
//    @Autowired
//    MimeMailMessage mimeMailMessage;

    /**
     * 普通文字
     *
     * @param title
     * @param text
     * @param from
     * @param to
     * @return
     */
    public boolean sendMessge(String title, String text, String from, String to) {


        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(text);
        simpleMailMessage.setBcc();
        simpleMailMessage.setFrom("1762861715@qq.com");
        simpleMailMessage.setTo("");
        mailSender.send(simpleMailMessage);
        return true;
    }

    public boolean sendMessge(String title, String text, String from) {


        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(text);
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setFrom("1762861715@qq.com");
        simpleMailMessage.setTo("");
        mailSender.send(simpleMailMessage);
        return true;
    }


}

package org.guge.coursebackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Random;

@SpringBootTest
class CourseBackendApplicationTests {
    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() {
    }

    @Test
    void mailSendTest() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("线上课堂-邮箱验证码");
        message.setText("您的验证码为: "+ getCode());
        message.setTo("hitrel@outlook.com");
        message.setFrom("1534595459@qq.com");

        mailSender.send(message);
    }

    String getCode() {
        StringBuilder s = new StringBuilder();
        var random = new Random();
        for (int i = 0; i < 6; i++) {
            s.append(random.nextInt(9));
        }

        return s.toString();
    }
}

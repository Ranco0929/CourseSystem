package org.guge.coursebackend.utils;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.service.UserService;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Random;

@Aspect
@Component
public class EmailUtils {
    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    UserService userService;

    @AfterReturning(returning = "result", pointcut = "execution(* org.guge.coursebackend.controller.UserController.register(..))")
    void mailSendTest(Result result) throws Exception {

        if (result.getCode() != ResultCode.SUCCESS.code) {
            return;
        }

        User user = (User) result.getData();

        String code = getCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("线上课堂-邮箱验证码");
        message.setText("您的验证码为: "+ code);
        message.setTo(user.getEmail());
        message.setFrom("1534595459@qq.com");

        mailSender.send(message);

        userService.setVerifyCode(user.getUserId(), code);
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

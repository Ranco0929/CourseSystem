package org.guge.coursebackend.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.service.UserService;
import org.guge.coursebackend.utils.HostInterface;
import org.guge.coursebackend.utils.TokenUtils;
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
public class EmailSender implements HostInterface {
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


        String url = "http://"+host+"/verify?token=";
        String code = getCode();
        var emailWithCode = new JSONObject();

        emailWithCode.put("email", user.getEmail());
        emailWithCode.put("code", code);

        var token = TokenUtils.createVerifyToken(emailWithCode.toJSONString());

        SimpleMailMessage message = new SimpleMailMessage();

        url += token;

        message.setSubject("Welcome to Guge-Course");
        message.setText("Thanks for signing up for a guge-course.org account! Please confirm your email address by clicking on the following link or pasting it into your browser: "+ url);
        message.setTo(user.getEmail());
        message.setFrom("do-not-reply@guge-course.org");

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

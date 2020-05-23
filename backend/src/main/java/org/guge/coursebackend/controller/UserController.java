package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSON;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.utils.annotation.CurrentUser;
import org.guge.coursebackend.utils.annotation.LoginRequire;
import org.guge.coursebackend.utils.exceptions.CourseException;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.guge.coursebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.create(user);
    }

    @DeleteMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@RequestBody long id) {
        return userService.delete(id);
    }

    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.update(user);
    }

    @LoginRequire
    @PostMapping(path = "/find", produces = "application/json;charset=utf-8")
    public Result find(@RequestBody String key) {
        return userService.find(JSON.parseObject(key));
    }

    @PostMapping(path = "/login")
    public Result login(@RequestBody @CurrentUser String user) {
        var loginParser = JSON.parseObject(user);
        return userService.login((String)loginParser.get("username"), (String)loginParser.get("password"));
    }

    @ExceptionHandler(CourseException.class)
    public @ResponseBody Result dealHandlerExceptionResolver() {
        return ResultFactory.buildResult(ResultCode.AUTHORIZATION, "Please login", "");
    }
}

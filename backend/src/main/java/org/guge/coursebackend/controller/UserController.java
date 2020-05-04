package org.guge.coursebackend.controller;

import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Binding;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/user/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.create(user);
    }

    @PostMapping(path = "/user/remove", produces = "application/json;charset=utf-8")
    public Result remove(@RequestBody long id) {
        return userService.delete(id);
    }

    @PostMapping(path = "/user/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.update(user);
    }
}

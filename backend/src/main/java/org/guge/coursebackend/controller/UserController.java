package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.entity.subentity.Role;
import org.guge.coursebackend.utils.TokenUtils;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Deprecated
    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.create(user);
    }

    @PostMapping(path = "/register", produces = "application/json;charset=utf-8")
    public Result register(@RequestBody String keyValue) {
        JSONObject detail = JSON.parseObject(keyValue);
        User user = new User();
        user.setName(detail.getString("name"));
        user.setEmail(detail.getString("email"));
        user.setRole(switch (detail.getString("role")) {
            case "teacher" -> Role.TEACHER;
            case "student" -> Role.STUDENT;
            default -> Role.STUDENT;
        });

        user.setPassword(detail.getString("password"));
        user.setInfo(detail.getString("info"));

        return userService.create(user);
    }

    @PostMapping(path = "/verify", produces = "application/json;charset=utf-8")
    public Result verify(@RequestParam String token) {
        JSONObject detail = JSON.parseObject(TokenUtils.parseToken(token).getId());
        return userService.verify(detail.getLong("email"), detail.getString("code"));
    }

    @Deprecated
    @DeleteMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@RequestBody long id) {
        return userService.delete(id);
    }

    @Deprecated
    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        }
        return userService.update(user);
    }

    @LoginRequire
    @GetMapping(path = "/get_info")
    public Result find(@RequestHeader("Authorization") String token) {
        Claims claims = TokenUtils.parseToken(token);
        return userService.search(claims.getId());
    }

    @LoginRequire
    @PostMapping(path = "/edit_info", produces = "application/json;charset=utf-8")
    public Result editInfo(@RequestHeader("Authorization") String token, @RequestBody String keyValue) {
        var claims = TokenUtils.parseToken(token);
        var detail = JSON.parseObject(keyValue);

        return userService.editInfo(claims.getId(), detail);
    }

    @LoginRequire
    @GetMapping(path = "/get_teachers", produces = "application/json;charset=utf-8")
    public Result getTeachers(@RequestHeader("Authorization") String token) {
        var claims = TokenUtils.parseToken(token);
        return userService.getTeachers(claims.getId());
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

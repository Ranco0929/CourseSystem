package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.Task;
import org.guge.coursebackend.utils.TokenUtils;
import org.guge.coursebackend.utils.annotation.LoginRequire;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.guge.coursebackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @LoginRequire
    @GetMapping(path = "/get_tasks")
    public Result getTasks(@RequestHeader("Authorization") String token, @RequestParam("courseId") long courseId, @RequestParam("userId") long userId) {
        var claims = TokenUtils.parseToken(token);
        return taskService.getTasks(courseId, userId, claims.getId());
    }

    @LoginRequire
    @GetMapping(path = "/get_task")
    public Result getTask(@RequestHeader("Authorization") String token, @RequestParam("taskId") long taskId) {
        var claims = TokenUtils.parseToken(token);
        return taskService.getTask(taskId, claims.getId());
    }

    @GetMapping(path = "/get_submissions")
    public Result getSubmissions(@RequestParam("taskId") long taskId) {
        return taskService.getSubmissions(taskId);
    }

    @LoginRequire
    @GetMapping(path = "/get_submission")
    public Result getSubmission(@RequestHeader("Authorization") String token, @RequestParam("taskId") long taskId, @RequestParam("userId") long userId) {
        var claims = TokenUtils.parseToken(token);
        return taskService.getSubmission(taskId, userId, claims.getId());
    }

    @LoginRequire
    @PostMapping(path = "/create_task")
    public Result createTask(@RequestHeader("Authorization") String token, @RequestBody String keyValue) {
        JSONObject task = JSON.parseObject(keyValue);
        var claims = TokenUtils.parseToken(token);

        return taskService.createTask(task, claims.getId());
    }

    @LoginRequire
    @PostMapping(path = "/update_task")
    public Result updateTask(@RequestHeader("Authorization") String token, @RequestBody String keyValue) {
        var claims = TokenUtils.parseToken(token);

        var task = JSON.parseObject(keyValue);

        return taskService.updateTask(task, claims.getId());
    }

    @DeleteMapping(path = "/delete_task")
    public Result deleteTask(@RequestHeader("Authorization") String token, @RequestParam("taskId") long taskId) {
        var claims = TokenUtils.parseToken(token);
        return taskService.deleteTask(taskId, claims.getId());
    }

    @PostMapping(path = "/submit_task")
    public Result submitTask(@RequestHeader("Ahthorization") String token, @RequestBody String submittask) {
        return taskService.submitTask(JSON.parseObject(submittask), TokenUtils.parseToken(token).getId());
    }

    @PostMapping(path = "/correct_task")
    public Result correctTask(@RequestHeader("Authorization") String token, @RequestBody String correction) {
        return taskService.correctTask(JSON.parseObject(correction), TokenUtils.parseToken(token).getId());
    }

    @GetMapping(path = "/get_task_analysis")
    public Result getTaskAnalysis(@RequestHeader("Authorization") String token, @RequestParam("taskId") long taskId) {
        return taskService.getTaskAnalysis(taskId, TokenUtils.parseToken(token).getId());
    }

    @Deprecated
    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskService.create(task);
        }
    }

    @Deprecated
    @PostMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@RequestBody JSONObject object) {
        if (object.containsKey("taskId")) {
            return taskService.delete((long)object.get("taskId"));
        } else {
            return ResultFactory.buildFailResult("invalid param" + object.toJSONString());
        }
    }

    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskService.update(task);
        }
    }

    //TODO find
}

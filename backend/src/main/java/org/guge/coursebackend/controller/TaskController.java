package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.Task;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskService.create(task);
        }
    }

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

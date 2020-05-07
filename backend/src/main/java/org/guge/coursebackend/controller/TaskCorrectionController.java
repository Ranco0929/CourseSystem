package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.TaskCorrection;
import org.guge.coursebackend.entity.subentity.TaskCorrectionKey;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.TaskCorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/task-correction")
public class TaskCorrectionController {
    @Autowired
    private TaskCorrectionService taskCorrectionService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody TaskCorrection taskCorrection, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskCorrectionService.create(taskCorrection);
        }
    }

    @DeleteMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@Valid @RequestBody TaskCorrectionKey taskCorrectionKey, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskCorrectionService.delete(taskCorrectionKey);
        }
    }

    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody TaskCorrection taskCorrection, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskCorrectionService.update(taskCorrection);
        }
    }

    //TODO find
}

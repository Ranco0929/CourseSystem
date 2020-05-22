package org.guge.coursebackend.controller;

import org.guge.coursebackend.entity.TaskSubmission;
import org.guge.coursebackend.entity.subentity.TaskSubmissionKey;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.guge.coursebackend.service.TaskSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/task-submission")
public class TaskSubmissionController {
    @Autowired
    private TaskSubmissionService taskSubmissionService;

    public Result create(@Valid @RequestBody TaskSubmission taskSubmission, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskSubmissionService.create(taskSubmission);
        }
    }

    public Result remove(@Valid @RequestBody TaskSubmissionKey taskSubmissionKey, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskSubmissionService.delete(taskSubmissionKey);
        }
    }

    public Result update(@Valid @RequestBody TaskSubmission taskSubmission, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return taskSubmissionService.update(taskSubmission);
        }
    }

    // TODO: 5/7/2020 find
}

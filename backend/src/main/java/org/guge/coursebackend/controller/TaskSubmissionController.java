package org.guge.coursebackend.controller;

import org.guge.coursebackend.service.TaskSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskSubmissionController {
    @Autowired
    private TaskSubmissionService taskSubmissionService;

    //TODO Implementation TaskSubmissionService
}

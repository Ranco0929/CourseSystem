package org.guge.coursebackend.controller;

import org.guge.coursebackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;
    //TODO implementation Task Controller
}

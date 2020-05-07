package org.guge.coursebackend.controller;

import org.guge.coursebackend.service.TaskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskAnalysisController {
    @Autowired
    private TaskAnalysisService taskAnalysisService;

    //TODO only find path access
}

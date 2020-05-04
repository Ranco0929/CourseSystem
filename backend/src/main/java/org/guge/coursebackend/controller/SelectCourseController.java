package org.guge.coursebackend.controller;

import org.guge.coursebackend.service.SelectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/selectCourse")
public class SelectCourseController {
    @Autowired
    private SelectCourseService selectCourseService;

    //TODO implementation Select CourseController
}

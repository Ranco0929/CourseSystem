package org.guge.coursebackend.controller;

import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors());
        } else {
            return courseService.create(course);
        }
    }

    //TODO Implementation Course Controller
}

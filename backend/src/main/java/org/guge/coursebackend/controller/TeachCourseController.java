package org.guge.coursebackend.controller;

import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.TeachCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/teach-course")
public class TeachCourseController {
    @Autowired
    private TeachCourseService teachCourseService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody TeachCourse teachCourse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return teachCourseService.create(teachCourse);
        }
    }

    @DeleteMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@Valid @RequestBody TeachCourseKey teachCourseKey, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return teachCourseService.delete(teachCourseKey);
        }
    }

    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody TeachCourse teachCourse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return teachCourseService.update(teachCourse);
        }
    }

    // TODO: 5/7/2020 find
}

package org.guge.coursebackend.controller;

import org.guge.coursebackend.entity.SelectCourse;
import org.guge.coursebackend.entity.subentity.SelectCourseKey;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.guge.coursebackend.service.SelectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.awt.geom.RectangularShape;

@RestController
@RequestMapping("/selectCourse")
public class SelectCourseController {
    @Autowired
    private SelectCourseService selectCourseService;

    @PostMapping(path = "/create", produces = "application/json;charset=utf-8")
    public Result create(@Valid @RequestBody SelectCourse selectCourse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return selectCourseService.create(selectCourse);
        }
    }

    @PostMapping(path = "/remove", produces = "application/json;charset=utf-8")
    public Result remove(@RequestBody SelectCourseKey selectCourseKey) {
        return selectCourseService.delete(selectCourseKey);
    }

    @PostMapping(path = "/update", produces = "application/json;charset=utf-8")
    public Result update(@Valid @RequestBody SelectCourse selectCourse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultFactory.buildFailResult(bindingResult.getAllErrors().get(0));
        } else {
            return selectCourseService.update(selectCourse);
        }
    }

    //TODO find
}

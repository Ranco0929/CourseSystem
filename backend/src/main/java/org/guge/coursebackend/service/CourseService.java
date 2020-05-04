package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.repository.CourseRepository;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Result create(Course course) {
        try {
            var it = courseRepository.findById(course.getCourseId());
            if (it.isEmpty()) {

                course.setCreatedAt(new Date());
                course.setUpdatedAt(new Date());

                courseRepository.save(course);

                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specificed Course Id has already existed: " + course.getCourseId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    //TODO Implementation CourseService
}

package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.repository.CourseRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
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

    public Result delete(long courseId) {
        try {
            var it = courseRepository.findById(courseId);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Can't find specificed course by id: " + courseId);
            } else {
                var tempCourse = it.get();
                tempCourse.setUpdatedAt(new Date());
                tempCourse.setEnable(false);
                courseRepository.save(tempCourse);

                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(Course course) {
        try {
            var it = courseRepository.findById(course.getCourseId());

            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("No Specified course by id: " + course.getCourseId());
            } else {
                course.setCreatedAt(it.get().getCreatedAt());
                course.setUpdatedAt(new Date());
                courseRepository.save(course);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    //TODO Course Entity Find Operate


}

package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.guge.coursebackend.repository.TeachCourseRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TeachCourseService {
    @Autowired
    private TeachCourseRepository teachCourseRepository;

    public Result create(TeachCourse teachCourse) {
        try {
            var it = teachCourseRepository.findById(teachCourse.getTeachCourseKey());
            if (it.isEmpty()) {
                teachCourse.setCreatedAt(new Date());
                teachCourse.setUpdatedAt(new Date());
                teachCourse.setEnable(true);

                teachCourseRepository.save(teachCourse);
                return ResultFactory.buildSuccessResult("success");
            } else {
                var tempRecord = it.get();
                if (tempRecord.isEnable())
                    return ResultFactory.buildFailResult("Specified teach record exists");
                else
                {
                    tempRecord.setEnable(true);
                    tempRecord.setUpdatedAt(new Date());
                    teachCourseRepository.save(tempRecord);
                    return ResultFactory.buildSuccessResult("success");
                }
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(TeachCourseKey teachCourseKey) {
        try {
            var it = teachCourseRepository.findById(teachCourseKey);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified teach record doesn't exists");
            } else {
                var tempRecord = it.get();

                if (tempRecord.isEnable()) {
                    tempRecord.setUpdatedAt(new Date());
                    tempRecord.setEnable(false);
                }

                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(TeachCourse teachCourse) {
        return ResultFactory.buildFailResult("Teach Record doesn't support update");
    }

    //TODO find
}

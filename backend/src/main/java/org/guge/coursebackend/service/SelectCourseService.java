package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.SelectCourse;
import org.guge.coursebackend.entity.subentity.SelectCourseKey;
import org.guge.coursebackend.repository.SelectCourseRepository;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SelectCourseService {
    @Autowired
    private SelectCourseRepository selectCourseRepository;

    public Result create(SelectCourse selectCourse) {
        try {
            var it = selectCourseRepository.findById(selectCourse.getSelectCourseKey());
            if (it.isEmpty()) {
                selectCourse.setCreatedAt(new Date());
                selectCourse.setUpdatedAt(new Date());
                selectCourse.setEnable(true);
                selectCourseRepository.save(selectCourse);
                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specified course has selected, user: " + selectCourse.getSelectCourseKey().getUserId() +" course: " + selectCourse.getSelectCourseKey().getCourseId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(SelectCourseKey selectCourseKey) {
        try {
            var it = selectCourseRepository.findById(selectCourseKey);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("No Specificied selected record. userId: " + selectCourseKey.getUserId() + " course: " + selectCourseKey.getCourseId());
            } else {
                var temp = it.get();
                temp.setUpdatedAt(new Date());
                temp.setEnable(false);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(SelectCourse selectCourse) {
        try {
            var it = selectCourseRepository.findById(selectCourse.getSelectCourseKey());
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("No specified record");
            } else {
                var temp = it.get();
                selectCourse.setCreatedAt(temp.getCreatedAt());
                selectCourse.setUpdatedAt(new Date());
                selectCourseRepository.save(selectCourse);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e ) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    //TODO SelectCourse Find Operate
}

package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.TaskCorrection;
import org.guge.coursebackend.entity.subentity.TaskCorrectionKey;
import org.guge.coursebackend.repository.TaskCorrectionRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskCorrectionService {
    @Autowired
    private TaskCorrectionRepository taskCorrectionRepository;

    public Result create(TaskCorrection taskCorrection) {
        try {
            var it = taskCorrectionRepository.findById(taskCorrection.getTaskCorrectionKey());
            if (it.isEmpty()) {
                taskCorrection.setCreatedAt(new Date());
                taskCorrection.setUpdatedAt(new Date());
                taskCorrection.setEnable(true);

                taskCorrectionRepository.save(taskCorrection);

                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specified correction exists");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(TaskCorrectionKey taskCorrectionKey) {
        return ResultFactory.buildFailResult("doesn't support delete task correction");
    }

    public Result update(TaskCorrection taskCorrection) {
        try {
            var it = taskCorrectionRepository.findById(taskCorrection.getTaskCorrectionKey());
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified correction doesn't exists");
            } else {
                var temp = it.get();
                taskCorrection.setCreatedAt(temp.getCreatedAt());
                taskCorrection.setUpdatedAt(new Date());

                taskCorrectionRepository.save(taskCorrection);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

}

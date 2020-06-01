package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.TaskSubmission;
import org.guge.coursebackend.entity.subentity.TaskSubmissionKey;
import org.guge.coursebackend.repository.TaskSubmissionRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskSubmissionService {
    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    public Result create(TaskSubmission taskSubmission) {
        try {
            var it = taskSubmissionRepository.findById(taskSubmission.getTaskSubmissionKey());
            if (it.isEmpty()) {
                taskSubmission.setCreatedAt(new Date());
                taskSubmission.setUpdatedAt(new Date());
                taskSubmission.setEnable(true);

                taskSubmissionRepository.save(taskSubmission);
                return ResultFactory.buildSuccessResult("success");
            } else {
                var tempSubmission = it.get();
                if (tempSubmission.isEnable()) {
                    return ResultFactory.buildFailResult("Specified submission exists");
                } else {
                    tempSubmission.setEnable(true);
                    tempSubmission.setUpdatedAt(new Date());
                    tempSubmission.setAnswer(taskSubmission.getAnswer());
                    tempSubmission.setAnswerState(taskSubmission.getAnswerState());
                    taskSubmissionRepository.save(tempSubmission);
                    return ResultFactory.buildSuccessResult("success");
                }
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(TaskSubmissionKey taskSubmissionKey) {
        try {
            var it = taskSubmissionRepository.findById(taskSubmissionKey);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified submission doesn't exists");
            } else {
                var tempSubmission = it.get();
                tempSubmission.setEnable(false);
                tempSubmission.setUpdatedAt(new Date());

                taskSubmissionRepository.save(tempSubmission);

                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(TaskSubmission taskSubmission) {
        try {
            var it = taskSubmissionRepository.findById(taskSubmission.getTaskSubmissionKey());

            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified submission doesn't exists");
            } else {
                var temp = it.get();
                taskSubmission.setCreatedAt(temp.getCreatedAt());
                taskSubmission.setUpdatedAt(new Date());
                taskSubmission.setEnable(true);

                taskSubmissionRepository.save(taskSubmission);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    //TODO find
}

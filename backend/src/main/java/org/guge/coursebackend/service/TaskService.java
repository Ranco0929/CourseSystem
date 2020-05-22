package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.Task;
import org.guge.coursebackend.repository.TaskRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Result create(Task task) {
        try {
            var it = taskRepository.findById(task.getTaskId());
            if (it.isEmpty()) {
                task.setCreatedAt(new Date());
                task.setUpdatedAt(new Date());
                task.setEnable(true);

                taskRepository.save(task);
                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specified task exists: " + task.getTaskId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(long taskId) {
        try {
            var it = taskRepository.findById(taskId);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified Task doesn't exists: " + taskId);
            } else {
                var tempTask = it.get();
                tempTask.setUpdatedAt(new Date());
                tempTask.setEnable(false);
                taskRepository.save(tempTask);

                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(Task task) {
        try {
            var it = taskRepository.findById(task.getTaskId());
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Specified Task doesn't exists" + task.getTaskId());
            } else {
                var tempTask = it.get();
                task.setCreatedAt(tempTask.getCreatedAt());
                task.setUpdatedAt(new Date());
                taskRepository.save(task);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    //TODO Task Find
}

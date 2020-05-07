package org.guge.coursebackend.service;

import org.guge.coursebackend.entity.TaskAnalysis;
import org.guge.coursebackend.repository.TaskAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskAnalysisService {
    @Autowired
    private TaskAnalysisRepository taskAnalysisRepository;

    public void create(TaskAnalysis taskAnalysis) throws Exception{
        var it = taskAnalysisRepository.findById(taskAnalysis.getTaskId());

        if (it.isEmpty()) {
            taskAnalysisRepository.save(taskAnalysis);
        } else {
            throw new Exception("exists: " + taskAnalysis.getTaskId());
        }
    }

    public void update(TaskAnalysis taskAnalysis) throws Exception {
        var it = taskAnalysisRepository.findById(taskAnalysis.getTaskId());

        if (it.isEmpty()) {
            throw new Exception("no such id: " + taskAnalysis.getTaskId());
        } else {
            taskAnalysisRepository.save(taskAnalysis);
        }
    }
}

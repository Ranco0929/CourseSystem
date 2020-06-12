package org.guge.coursebackend.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.guge.coursebackend.entity.TaskAnalysis;
import org.guge.coursebackend.repository.TaskAnalysisRepository;
import org.guge.coursebackend.repository.TaskCorrectionRepository;
import org.guge.coursebackend.repository.TaskRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CorrectionAnalysis {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAnalysisRepository taskAnalysisRepository;

    @AfterReturning(returning = "result", pointcut = "execution(* org.guge.coursebackend.service.TaskService.correctTask(correction, email))")
    public void autoSummary(Result result, JSONObject correction, String email) throws Exception {
        if (result.getCode() != ResultCode.SUCCESS.code) {
            return;
        }
        var taskId = correction.getLong("taskId");

        var task_content = taskRepository.findById(taskId).orElseThrow().getContent();

        var it = taskAnalysisRepository.findById(taskId);

        TaskAnalysis new_analysis;
        JSONObject new_content;
        if (it.isEmpty()) {
            new_analysis = new TaskAnalysis();
            new_analysis.setTaskId(taskId);

            new_content = new JSONObject();
        } else {
            new_analysis = it.get();

            new_content = new_analysis.getFaultsSummaries();
        }


        correction.getJSONObject("content").forEach((key, value) -> {

            float grade = 0;

            if (!new_content.containsKey(key)) {
                var new_result = new JSONObject();
                new_result.put("true", 0);
                new_result.put("false", 0);

                new_content.put(key, new_result);
            }

            if (task_content.containsKey(key)) {
                var quiz = task_content.getJSONObject(key);
                grade = quiz.getFloat("grade");

                var new_result = new_content.getJSONObject(key);
                if (((JSONObject) value).getFloat("grade") < grade) {
                    new_result.put("false", new_result.getInteger("false") + 1);
                } else {
                    new_result.put("true", new_result.getInteger("true") + 1);
                }

                new_content.put(key, new_result);
            }

        });

        new_analysis.setFaultsSummaries(new_content);

        taskAnalysisRepository.save(new_analysis);

    }
}

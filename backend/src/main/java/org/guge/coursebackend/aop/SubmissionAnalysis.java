package org.guge.coursebackend.aop;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.guge.coursebackend.entity.TaskAnalysis;
import org.guge.coursebackend.entity.TaskSubmission;
import org.guge.coursebackend.repository.TaskAnalysisRepository;
import org.guge.coursebackend.repository.TaskRepository;
import org.guge.coursebackend.repository.TaskSubmissionRepository;
import org.guge.coursebackend.service.TaskSubmissionService;
import org.guge.coursebackend.utils.SimilarityResolve;
import org.guge.coursebackend.utils.exceptions.CourseException;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SubmissionAnalysis {
    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAnalysisRepository taskAnalysisRepository;

    @AfterReturning(returning = "result", pointcut = "execution(* org.guge.coursebackend.service.TaskService.submitTask(..)) && args(taskSubmission, email)", argNames = "result,taskSubmission,email")
    public void autoSimilarityResolve(Result result, JSONObject taskSubmission, String email) throws Exception {
        if (result.getCode() != ResultCode.SUCCESS.code) {
            return;
        }

        var taskId = taskSubmission.getLong("taskId");

        var targetAnswer = taskSubmission.getJSONObject("answer");
        var targetUserId = taskSubmission.getLong("userId");

        var it = taskRepository.findById(taskId);

        var analysis_it = taskAnalysisRepository.findById(taskId);
        TaskAnalysis analysis;
        if (analysis_it.isEmpty()) {
            analysis = new TaskAnalysis();
            analysis.setTaskId(taskId);
        } else {
            analysis = analysis_it.get();
        }
        var analysisSimilarity = analysis.getSimilarity();

        var submission_list = taskSubmissionRepository.findAllByTaskId(taskId);

        if (it.isEmpty()) {
            throw new CourseException(ResultCode.NOTFOUND,"No task find by similarity resolve, taskId:" + taskId);
        } else {
            var task = it.get();
            var content = task.getContent();

            content.forEach((k, v) -> {
                if (v instanceof JSONObject) {
                    if (((JSONObject) v).getInteger("type") > 3) { // type 大于 3 则为简答题和应用题，需要进行相似度检查
                        double similarity = 0;
                        var targetString = targetAnswer.getJSONObject(k).getString("content");
                        int similarityCount = 0;
                        for (var sourceSubmission : submission_list) {
                            if (sourceSubmission.getTaskSubmissionKey().getUserId() == targetUserId) {
                                continue;
                            }
                            var sourceString = sourceSubmission.getAnswer().getJSONObject(k).getString("content");

                            similarity += SimilarityResolve.resolve(targetString, sourceString);
                            similarityCount += 1;
                        }

                        similarity /= similarityCount;
                        if (analysisSimilarity.containsKey(k)) {
                            var k_question = analysisSimilarity.getJSONObject(k);
                            k_question.put("amount", k_question.getInteger("amount") + 1);
                            var k_detail = k_question.getJSONObject("detail");
                            if (similarityCount == 0) {
                                k_detail.put(targetUserId.toString(), 0);
                            } else {
                                k_detail.put(targetUserId.toString(), similarity);
                            }

                            k_question.put("detail", k_detail);
                            analysisSimilarity.put(k, k_question);
                        }
                    }
                }
            });

            analysis.setSimilarity(analysisSimilarity);

            taskAnalysisRepository.save(analysis);

        }


    }
}

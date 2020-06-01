package org.guge.coursebackend.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.Task;
import org.guge.coursebackend.entity.TaskCorrection;
import org.guge.coursebackend.entity.TaskSubmission;
import org.guge.coursebackend.entity.subentity.*;
import org.guge.coursebackend.repository.*;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;
    
    @Autowired
    private TaskCorrectionRepository taskCorrectionRepository;

    @Autowired
    private TaskAnalysisRepository taskAnalysisRepository;

    public Result getTask(long taskId, String email) {
        try {
            var it = userRepository.findByEmail(email);
            if (it.isEmpty()) {
                return ResultFactory.NotFoundResult("User with email: " + email + " Not Found");
            }

            var user = it.get();
            return switch (user.getRole()) {
                case TEACHER -> getTeachersTask(taskId);
                case STUDENT -> getStudentsTask(taskId);
            };
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    private Result getTeachersTask(long taskId) throws Exception {
        var it = taskRepository.findById(taskId);

        if (it.isEmpty()) {
            return ResultFactory.NotFoundResult("Task with id: " + taskId + " Not Found");
        }

        return ResultFactory.buildSuccessResult(it.get());
    }

    private Result getStudentsTask(long taskId) throws Exception {
        var it = taskRepository.findById(taskId);

        if (it.isEmpty()) {
            return ResultFactory.NotFoundResult("Task whit id: " + taskId + " Not Found");
        }
        var task = it.get();

        task = removeAnswer(task);

        return ResultFactory.buildSuccessResult(task);
    }

    private Task removeAnswer(Task task) throws Exception {
        task.setSolution(null);
        return task;
    }

    public Result getTasks(long courseId, long userId, String email) {
        try {
            var user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return ResultFactory.NotFoundResult("User: " + userId + "Not Found");
            } else {
                var user_exist = user.get();
                if (!user_exist.getEmail().equals(email)) {
                    return ResultFactory.buildAuthorzationFailedResult("email: " + email + "Error when compare with userId: " + userId);
                }

                return switch (user_exist.getRole()) {
                    case TEACHER -> getTeachersTasks(courseId);
                    case STUDENT -> getStudentsTasks(courseId);
                };
            }
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    private Result getTeachersTasks(long courseId) throws Exception {
        try {
            var list = taskRepository.findAllByCourseId(courseId);
            if (list.size() <= 0) {
                return ResultFactory.buildFailResult("Wrong course");
            }
            return ResultFactory.buildSuccessResult(list);
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    private Result getStudentsTasks(long courseId) throws Exception {
        try {
            var list = taskRepository.findAllByCourseId(courseId);
            if (list.size() <= 0) {
                return ResultFactory.buildFailResult("Wrong course");
            }
            list = removeAnswer(list);

            return ResultFactory.buildSuccessResult(list);
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    private List<Task> removeAnswer(List<Task> list) throws Exception {
        for (var task :
                list) {
            task.setSolution(null);
        }
        return list;
    }

    public Result getSubmission(long taskId, long userId, String email) {
        try {
            var it = userRepository.findById(userId);

            if (it.isEmpty()) {
                return ResultFactory.NotFoundResult("User with id: " + userId + " Not Found");
            }
            var user = it.get();
            if (!user.getEmail().equals(email)) {
                return ResultFactory.buildAuthorzationFailedResult("Error account");
            }
            var taskit = taskSubmissionRepository.findById(new TaskSubmissionKey(taskId, userId));

            if (taskit.isEmpty()) {
                return ResultFactory.buildFailResult("Wrong task id");
            }
            return ResultFactory.buildSuccessResult(taskit.get());
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result getSubmissions(long taskId) {
        try {
            var list = taskSubmissionRepository.findAllByTaskId(taskId);

            if (list.size() <= 0) {
                return ResultFactory.NotFoundResult("wrong task id: " + taskId);
            }

            return ResultFactory.buildSuccessResult(list);
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    @Deprecated
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

    public Result createTask(JSONObject task, String email) {
        try {
            var newTask = new Task();
            newTask.setCourseId(task.getLong("courseId"));
            newTask.setTitle(task.getString("title"));
            setContent(newTask, task.getString("content"));
            setSolution(newTask, task.getString("solution"));

            newTask.setTaskState(switch (task.getInteger("state")) {
                case 0 -> TaskState.UNPUBLISHED;
                case 1 -> TaskState.PUBLISHED;
                default -> throw new Exception("unknown state: " + task.getInteger("state"));
            });

            newTask.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(task.getString("deadline")));

            newTask.setCreatedAt(new Date());
            newTask.setUpdatedAt(new Date());

            taskRepository.save(newTask);

            return ResultFactory.buildSuccessResult(newTask);
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    private void setContent(Task task, String json) throws Exception {
        task.setContent(JSON.parseObject(json));

    }

    private void setSolution(Task task, String json) throws Exception {
        task.setSolution(JSON.parseObject(json));
    }

    public Result updateTask(JSONObject task, String email) {
       try {
           var it = userRepository.findByEmail(email);
           if (it.isEmpty()) {
               return ResultFactory.NotFoundResult("User with email: " + email + " Not Found");
           }

           var user = it.get();
           if (!user.getEmail().equals(email)) {
               return ResultFactory.buildAuthorzationFailedResult("Error Authorization token");
           }

           if (user.getRole() == Role.STUDENT) {
               return ResultFactory.buildAuthorzationFailedResult("Unable to update");
           }

           var taskit = taskRepository.findById(task.getLong("taskId"));

           if (taskit.isEmpty()) {
               return ResultFactory.buildFailResult("Wrong task id");
           }

           var exists_task = taskit.get();

           exists_task.setTitle(task.getString("titile"));
           exists_task.setContent(task.getJSONObject("content"));
           exists_task.setSolution(task.getJSONObject("solution"));
           exists_task.setTaskState(switch (task.getInteger("state")){
               case 0 -> TaskState.UNPUBLISHED;
               case 1 -> TaskState.PUBLISHED;
               default -> throw new Exception("unknown task state: " + task.getString("state"));
           });

           exists_task.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(task.getString("deadline")));
           exists_task.setUpdatedAt(new Date());
           taskRepository.save(exists_task);

           return ResultFactory.buildSuccessResult(exists_task);
       } catch (Exception e) {
           return ResultFactory.ServerErrorResult(e.getMessage());
       }
    }

    public Result deleteTask(long taskId, String email) {
        try {
            var it = userRepository.findByEmail(email);
            if (it.isEmpty()) {
                return ResultFactory.buildAuthorzationFailedResult("Error Authorization token");
            }
            var user = it.get();
            if (user.getRole() == Role.STUDENT) {
                return ResultFactory.buildFailResult("unable to delete");
            }

            var task = taskRepository.findById(taskId).orElseThrow();

            task.setEnable(false);

            return ResultFactory.buildSuccessResult("");
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result submitTask(JSONObject submitTask, String email) {
        try {
            var it = userRepository.findByEmail(email);

            if (it.isEmpty()) {
                return ResultFactory.buildAuthorzationFailedResult("Error Authorization token");
            }

            var user = it.get();
            if (!user.getEmail().equals(email) || user.getUserId() != submitTask.getLong("userId")) {
                return ResultFactory.NotFoundResult("error userId");
            }
            var key = new TaskSubmissionKey(submitTask.getLong("taskId"), submitTask.getLong("userId"));
            var submission = taskSubmissionRepository.findById(key);

            TaskSubmission newSubmission = null;
            if (submission.isEmpty()) {
                newSubmission = new TaskSubmission();
                newSubmission.setTaskSubmissionKey(key);
                newSubmission.setCreatedAt(new Date());
            } else {
                newSubmission = submission.get();
            }
            newSubmission.setAnswer(submitTask.getJSONObject("answer"));
            newSubmission.setAnswerState(switch (submitTask.getInteger("state")){
                case 0 -> AnswerState.UNSUBMITTED;
                case 1 -> AnswerState.SUBMITTED;
                default -> throw new Exception("unknown answer state: " + submitTask.getString("state"));
            });

            newSubmission.setUpdatedAt(new Date());
            newSubmission.setEnable(true);

            taskSubmissionRepository.save(newSubmission);

            return ResultFactory.buildSuccessResult("");



        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result correctTask(JSONObject correctTask, String email) {
        try {
            var it = userRepository.findByEmail(email);

            if (it.isEmpty()) {
                return ResultFactory.buildAuthorzationFailedResult("Error Authorization token");
            }

            var user = it.get();
            if (!user.getEmail().equals(email)) {
                return ResultFactory.NotFoundResult("error userId");
            } else if (user.getRole() == Role.STUDENT) {
                return ResultFactory.buildFailResult("Unable to correct");
            }
            var key = new TaskCorrectionKey(correctTask.getLong("taskId"), correctTask.getLong("userId"));
            var correction = taskCorrectionRepository.findById(key);
            TaskCorrection taskCorrection = null;

            if (correction.isEmpty()) {
                taskCorrection.setTaskCorrectionKey(key);
                taskCorrection.setCreatedAt(new Date());
            } else {
                taskCorrection = correction.get();
            }

            taskCorrection.setContent(correctTask.getJSONObject("content"));
            taskCorrection.setCorrectionState(switch (correctTask.getInteger("state")) {
                case 0 -> CorrectionState.UNCORRECTED;
                case 1 -> CorrectionState.CORRECTED;
                case 2 -> CorrectionState.REFUSE;
                default -> throw new Exception("unknown correction state: " + correctTask.getString("state"));
            });

            taskCorrection.setUpdatedAt(new Date());

            return ResultFactory.buildSuccessResult("");
        }catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result getTaskAnalysis(long taskId, String email) {
        try {
            var it = userRepository.findByEmail(email);

            if (it.isEmpty()) {
                return ResultFactory.buildAuthorzationFailedResult("Error Authorization token");
            }

            var user = it.get();
            if (!user.getEmail().equals(email)) {
                return ResultFactory.NotFoundResult("error userId");
            } else if (user.getRole() == Role.STUDENT) {
                return ResultFactory.buildFailResult("unable to get analysis");
            }

            var analysis = taskAnalysisRepository.findById(taskId);

            if (analysis.isEmpty()) {
                return ResultFactory.NotFoundResult("Wrong task id");
            }

            return ResultFactory.buildSuccessResult(analysis.get());
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    @Deprecated
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

    @Deprecated
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

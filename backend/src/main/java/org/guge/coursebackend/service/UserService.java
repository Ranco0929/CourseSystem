package org.guge.coursebackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.repository.SelectCourseRepository;
import org.guge.coursebackend.repository.TeachCourseRepository;
import org.guge.coursebackend.repository.UserRepository;
import org.guge.coursebackend.utils.TokenUtils;
import org.guge.coursebackend.utils.exceptions.ServerErrorException;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SelectCourseRepository selectCourseRepository;

    @Autowired
    private TeachCourseRepository teachCourseRepository;

    public Result findAll() {
        try {
            var it = userRepository.findAll();
            var users = new ArrayList<User>();
            it.forEach(e -> {
                if (e.isEnable()) {
                    users.add(e);
                }
            });

            return ResultFactory.buildSuccessResult(users);
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result create(User user) {
        try {
            var it = userRepository.findById(user.getUserId());
            if (it.isEmpty()) {
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());
                user.setEnable(true);
                userRepository.save(user);

                return ResultFactory.buildSuccessResult(userRepository.findByEmail(user.getEmail()).orElseThrow());
            } else {
                return ResultFactory.buildFailResult("Specific Id has created: " + user.getUserId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result update(User user) {
        try {
            var it = userRepository.findById(user.getUserId());
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("Cannot find User By Id: " + user.getUserId());
            } else {
                user.setCreatedAt(it.get().getCreatedAt());
                user.setUpdatedAt(new Date());
                userRepository.save(user);
                return  ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(long id) {
        try {

            var it = userRepository.findById(id);
            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("No value present");
            } else {

                var user = it.get();
                user.setUpdatedAt(new Date());
                user.setEnable(false);
            }
            return ResultFactory.buildSuccessResult("success");
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result login(String email, String password) {
        var it = userRepository.findByEmailAndPassword(email, password);

        if (it.isEmpty()) {
            return ResultFactory.buildResult(ResultCode.AUTHORIZATION, "Can't find User: " + email, "");
        } else {
            String token = TokenUtils.createToken(email);
            var user = it.get();
            user.setToken(token);
            return ResultFactory.buildSuccessResult(user);
        }
    }

    public Result search(String email) {
        var it = userRepository.findByEmail(email);

        if (it.isEmpty()) {
            return ResultFactory.buildResult(ResultCode.SERVERERROR, "server error", "");
        } else {
            return ResultFactory.buildSuccessResult(it.get());
        }
    }

    public void setVerifyCode(long userId, String code) throws ServerErrorException {
        var it = userRepository.findById(userId);
        if (!it.isEmpty()) {
            var user = it.get();
            user.setVerifiedCode(code);

            userRepository.save(user);
            return;
        }

        throw new ServerErrorException();
    }

    public Result verify(long userId, String code) {
        try {
            var it = userRepository.findById(userId);
            if (it.isEmpty()) {
                return ResultFactory.buildResult(ResultCode.NOTFOUND, "User Not Found", "");
            } else {
                var user = it.get();
                if (user.getVerifiedCode().equals(code)) {
                    return ResultFactory.buildSuccessResult("");
                } else {
                    return ResultFactory.buildResult(ResultCode.VERIFYCODEERROR, "wrong verify code", "");
                }
            }
        } catch (Exception e) {
            return ResultFactory.buildResult(ResultCode.SERVERERROR, "server error", e.getMessage());
        }
    }

    public Result editInfo(String email, JSONObject detail) {
        try {
            var it = userRepository.findById(detail.getLong("userId"));
            if (it.isEmpty()) {
                return ResultFactory.NotFoundResult(null);
            } else {
                var user = it.get();
                if (user.getEmail().equals(email)) {
                    user.setName(detail.getString("name"));
                    user.setInfo(detail.getString("info"));
                    user.setUpdatedAt(new Date());
                    userRepository.save(user);
                    return ResultFactory.buildSuccessResult(user);
                } else {
                    return ResultFactory.buildAuthorzationFailedResult("Can't edit user: " + email);
                }
            }
        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result getTeachers(String email) {
        try {
            var it = userRepository.findByEmail(email);
            if (it.isEmpty()) {
                return ResultFactory.NotFoundResult("Can't found user: " + email);
            } else {
                var id = it.get().getUserId();

                var courseList = selectCourseRepository.findAllByUserId(id);

                Map<Long, JSONObject> result = new HashMap<>();
                for (var course : courseList) {
                    var teacherlist = teachCourseRepository.findAllByCourseId(course.getSelectCourseKey().getCourseId());
                    for (var teacher :
                            teacherlist) {
                        var teacherId = teacher.getTeachCourseKey().getUserId();
                        if (!result.containsKey(teacherId)) {
                            JSONObject keyValue = new JSONObject();
                            var new_teacher = userRepository.findById(teacherId).orElseThrow();
                            keyValue.put("userId", new_teacher.getUserId());
                            keyValue.put("name", new_teacher.getName());
                            keyValue.put("email", new_teacher.getEmail());
                            result.put(teacherId, keyValue);
                        }
                    }
                }
                JSONArray array = new JSONArray();
                result.forEach((k, v) -> {
                    array.add(v);
                });

                return ResultFactory.buildSuccessResult(array);
            }

        } catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

}



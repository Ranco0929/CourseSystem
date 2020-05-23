package org.guge.coursebackend.service;

import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.repository.UserRepository;
import org.guge.coursebackend.utils.TokenUtils;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

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

                return ResultFactory.buildSuccessResult("success");
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

    public Result find(JSONObject o) {
        String s = "SELECT userId, email, password, verifiedCode, name, info, avator, createdAt, updatedAt FROM User where ";

        if (o.isEmpty()) {
            return findAll();
        }

        if (o.containsKey("key")) {
            String key = (String) o.get("key");
            s += key;
            if (o.containsKey("range")) {
                JSONObject range = (JSONObject) o.get("range");
                s +=  " < " + range.get("gt") + " AND " + key + " >= " + range.get("lt");
            }

            if (o.containsKey("value")) {
                s += " = " + o.get("value");
            }
        }
        var query = em.createQuery(s);
        return ResultFactory.buildSuccessResult(query.getResultStream());
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

    public User findByEmail(String email) {
        var it = userRepository.findByEmail(email);

        if (it.isEmpty()) {
            return null;
        } else {
            return it.get();
        }
    }
}



package org.guge.coursebackend.service;

import jdk.jshell.spi.ExecutionControl;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.repository.UserRepository;
import org.guge.coursebackend.result.Result;
import org.guge.coursebackend.result.ResultFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Result findAll() {
        try {
            var it = userRepository.findAll();
            var users = new ArrayList<User>();
            it.forEach(e -> {
                if (e.Enable()) {
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

    public Result find(Map o) {
        try {
            var users = findAll();

        } catch (Exception e) {

        }
        throw new NotYetImplementedException();
    }
}

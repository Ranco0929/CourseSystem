package org.guge.coursebackend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.service.CourseService;
import org.guge.coursebackend.utils.TokenUtils;
import org.guge.coursebackend.utils.annotation.LoginRequire;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController{

    @Autowired
    private CourseService courseService;

    @LoginRequire
    @GetMapping(path = "/get_courses", produces = "application/json;charset=utf-8")
    public Result getCourses(@RequestParam("userId") Long userId){
        return courseService.getCourses(userId);
    }

    @LoginRequire
    @PostMapping(path = "/create_course",produces = "application/json;charset=utf-8")
    public Result createCourse(@RequestBody String course ){
        JSONObject obj = JSON.parseObject(course);
        String name = obj.getString("name");
        String info = obj.getString("info");
        String avatar = obj.getString("avatar");
        Long creator = obj.getLong("creator");
        JSONArray teachers =obj.getJSONArray("teachers");
        return courseService.create(name,info,avatar,creator,teachers);
    }

    @PostMapping(path = "/add_course",produces = "application/json;charset=utf-8")
    public Result addCourse(@RequestBody String course){
        JSONObject obj = JSON.parseObject(course);
        var courseId = obj.getLong("courseId");
        var userId = obj.getLong("userId");
        return courseService.add(courseId,userId);

    }

    @PostMapping(path = "/delete_Course",produces = "application/json;charset=utf-8")
    public Result deleteCourse(@RequestBody String course){
        JSONObject obj = JSON.parseObject(course);
        var courseId = obj.getLong("courseId");
        var userId = obj.getLong("userId");
        if(obj.containsKey("courseId")){
            return courseService.delete(courseId,userId);
        }else{
            return ResultFactory.buildFailResult("Invalid Request Body:" + obj.toJSONString());
        }
    }

    @GetMapping(path = "/get_course",produces = "application/json;charset=utf-8")
    public Result getCourse(@RequestParam("courseId") Long courseId, @RequestParam("userId") Long userId){
          return courseService.getCourse(courseId,userId);
    }

    @GetMapping(path = "/get_students",produces = "application/json;charset=utf-8")
    public Result getStudents(@RequestParam("courseId") Long courseId){
        return courseService.getStudents(courseId);

    }
}
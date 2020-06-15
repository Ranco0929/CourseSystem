package org.guge.coursebackend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.entity.SelectCourse;
import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.SelectCourseKey;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.guge.coursebackend.repository.CourseRepository;
import org.guge.coursebackend.repository.SelectCourseRepository;
import org.guge.coursebackend.repository.TeachCourseRepository;
import org.guge.coursebackend.repository.UserRepository;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SelectCourseRepository selectCourseRepository;

    @Autowired
    private TeachCourseRepository teachCourseRepository;

    public Result getCourses(Long userId){
        try{
            var it = userRepository.findById(userId);
            if(it.isEmpty()){
                return ResultFactory.NotFoundResult("user id not found");
            }else{
                var id = it.get().getUserId();
                var courseList = selectCourseRepository.findAllByCourseId(id);
                Map<Long , JSONObject> result = new HashMap<>();
                for(var selectCourse : courseList){
                    var courseId = selectCourse.getSelectCourseKey().getCourseId();
                    if(!result.containsKey(courseId)){
                        JSONObject keyValue = new JSONObject();
                        var new_course = courseRepository.findById(courseId).orElseThrow();
                        keyValue.put("courseId", new_course.getCourseId());
                        keyValue.put("name", new_course.getName());
                        keyValue.put("avatar", new_course.getAvatar());
                        Map<Long , JSONObject> teacherresult = new HashMap<>();
                        var teacherlist = teachCourseRepository.findAllByCourseId(selectCourse.getSelectCourseKey().getCourseId());
                        for (var teacher : teacherlist) {
                            var teacherId = teacher.getTeachCourseKey().getUserId();
                            if (!result.containsKey(teacherId)) {
                                JSONObject keyValuesecond = new JSONObject();
                                var new_teacher = userRepository.findById(teacherId).orElseThrow();
                                keyValuesecond.put("userId", new_teacher.getUserId());
                                keyValuesecond.put("name", new_teacher.getName());
                                keyValuesecond.put("info", new_teacher.getInfo());
                                keyValuesecond.put("email", new_teacher.getEmail());
                                keyValuesecond.put("role", new_teacher.getRole());
                                keyValuesecond.put("createdAt", new_teacher.getCreatedAt());
                                keyValuesecond.put("updatedAt", new_teacher.getUpdatedAt());
                                teacherresult.put(teacherId,keyValuesecond);
                            }
                        }
                        JSONArray arrayteacher = new JSONArray();
                        teacherresult.forEach((k, v) -> {
                            arrayteacher.add(v);
                        });
                        keyValue.put("teachers",arrayteacher);
                        keyValue.put("creator", new_course.getCreator());
                        keyValue.put("info", new_course.getInfo());
                        keyValue.put("createdAt", new_course.getCreatedAt());
                        result.put(userId, keyValue);
                    }
                }
                JSONArray array = new JSONArray();
                result.forEach((k, v) -> {
                    array.add(v);
                });
                return ResultFactory.buildSuccessResult(array);
            }
        }catch(Exception e){
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result create(String coursename, String info, String avatar, Long creator, JSONArray teachers) {
        try {
            var course = new Course();

            var it = courseRepository.findByName(coursename);
            if (it.isEmpty()) {
                course.setName(coursename);
                course.setAvatar(avatar);
                course.setInfo(info);
                course.setCreator(creator);
                course.setCreatedAt(new Date());
                course.setTeachers(teachers.toJavaList(Long.class));
                courseRepository.save(course);
                for(var teacher : teachers.toJavaList(Long.class)){
                    var teachcourse = new TeachCourse();
                    var teachcoursekey = new TeachCourseKey();
                    teachcoursekey.setCourseId(course.getCourseId());
                    teachcoursekey.setUserId(teacher);
                    teachcourse.setTeachCourseKey(teachcoursekey);
                    teachcourse.setCreatedAt(new Date());
                    teachcourse.setUpdatedAt(new Date());
                    teachCourseRepository.save(teachcourse);
                }
                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specificed Course Id has already existed: " + course.getCourseId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result add(long cId,long uId){
        try{
            var selectcourse = new SelectCourse();
            var selectcoursekey = new SelectCourseKey();
            selectcoursekey.setCourseId(cId);
            selectcoursekey.setUserId(uId);
            var it = selectCourseRepository.findById(selectcoursekey);
            if(it.isEmpty()){
                selectcourse.setSelectCourseKey(selectcoursekey);
                selectCourseRepository.save(selectcourse);
                return ResultFactory.buildSuccessResult("success");
            }else{
                return ResultFactory.buildFailResult("wrong course id or user id");
            }
        }catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(long courseId, long userId) {
        try {
            var it = courseRepository.findById(courseId);
            var user = userRepository.findById(userId).orElseThrow();
            switch (user.getRole()) {
                case STUDENT -> deleteselectCourse(userId,courseId);
                case TEACHER -> {
                    deleteTeachCourse(courseId);
                    deleteCourse(courseId);
                    deleteSelectAllCourse(courseId);
                }
            }
            return ResultFactory.buildSuccessResult("success");
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    private void deleteTeachCourse(long courseId) throws Exception {
        var teach = teachCourseRepository.findAllByCourseId(courseId);
        for(var tempteachCourse : teach){
            tempteachCourse.setUpdatedAt(new Date());
            tempteachCourse.setEnable(false);
            teachCourseRepository.save(tempteachCourse);
        }
    }

    private void deleteselectCourse(long userId, long courseId) throws  Exception{
        var key = new SelectCourseKey(userId, courseId);
        var it = selectCourseRepository.findById(key);
        var tempselectCourse = it.get();
        tempselectCourse.setUpdatedAt(new Date());
        tempselectCourse.setEnable(false);
        selectCourseRepository.save(tempselectCourse);
    }

    private  void deleteSelectAllCourse(long courseId) throws  Exception{
        var selectCourseList = selectCourseRepository.findAllByCourseId(courseId);
        for(var tempSelectCourse : selectCourseList){
            tempSelectCourse.setUpdatedAt(new Date());
            tempSelectCourse.setEnable(false);
            selectCourseRepository.save(tempSelectCourse);
        }
    }

    private void deleteCourse(long courseId){
        var it = courseRepository.findById(courseId);
        var tempcourse = it.get();
        tempcourse.setUpdatedAt(new Date());
        tempcourse.setEnable(false);
        courseRepository.save(tempcourse);
    }

    public Result update(Course course) {
        try {
            var it = courseRepository.findById(course.getCourseId());

            if (it.isEmpty()) {
                return ResultFactory.buildFailResult("No Specified course by id: " + course.getCourseId());
            } else {
                course.setCreatedAt(it.get().getCreatedAt());
                course.setUpdatedAt(new Date());
                courseRepository.save(course);
                return ResultFactory.buildSuccessResult("success");
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result getCourse(long courseId,long userId){
        try {
            var it = courseRepository.findById(courseId);
            if (it.isEmpty()) {
                return ResultFactory.NotFoundResult("wrong course id");
            } else {
                Map<Long , JSONObject> result = new HashMap<>();
                JSONObject keyValue = new JSONObject();
                keyValue.put("courseId",courseId);
                keyValue.put("name",it.get().getName());
                keyValue.put("avatar",it.get().getAvatar());
                Map<Long , JSONObject> teacherresult = new HashMap<>();

                var teacherlist = teachCourseRepository.findAllByCourseId(courseId);
                for (var teacher : teacherlist) {
                    var teacherId = teacher.getTeachCourseKey().getUserId();
                    if (!teacherresult.containsKey(teacherId)) {
                        JSONObject keyValuesecond = new JSONObject();
                        var new_teacher = userRepository.findById(teacherId).orElseThrow();
                        keyValuesecond.put("userId", new_teacher.getUserId());
                        keyValuesecond.put("name", new_teacher.getName());
                        keyValuesecond.put("info", new_teacher.getInfo());
                        keyValuesecond.put("email", new_teacher.getEmail());
                        keyValuesecond.put("role", new_teacher.getRole());
                        keyValuesecond.put("createdAt", new_teacher.getCreatedAt());
                        keyValuesecond.put("updatedAt", new_teacher.getUpdatedAt());
                        teacherresult.put(teacherId,keyValuesecond);
                    }
                }
                JSONArray arrayteacher = new JSONArray();
                teacherresult.forEach((k, v) -> {
                    arrayteacher.add(v);
                });
                keyValue.put("teachers",arrayteacher);
                keyValue.put("creator",it.get().getCreator());
                keyValue.put("info",it.get().getInfo());
                keyValue.put("createdAt",it.get().getCreatedAt());
                result.put(courseId, keyValue);
                JSONArray array = new JSONArray();
                result.forEach((k, v) -> {
                    array.add(v);
                });
                return ResultFactory.buildSuccessResult(array);
            }
            }catch(Exception e){
                e.printStackTrace();
                return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

    public Result getStudents(long courseId){
        try{
            var id = courseId;
            var courseList = selectCourseRepository.findAllByUserId(id);
            Map<Long, JSONObject> result = new HashMap<>();
            for (var course : courseList) {
                var studentlist = teachCourseRepository.findAllByCourseId(course.getSelectCourseKey().getCourseId());
                for (var student : studentlist) {
                    var studentId = student.getTeachCourseKey().getUserId();
                    if (!result.containsKey(studentId)) {
                        JSONObject keyValue = new JSONObject();
                        var new_student = userRepository.findById(studentId).orElseThrow();
                        keyValue.put("userId", new_student.getUserId());
                        keyValue.put("name", new_student.getName());
                        keyValue.put("info", new_student.getInfo());
                        keyValue.put("email", new_student.getEmail());
                        keyValue.put("createdAt",new_student.getCreatedAt());
                        keyValue.put("updatedAt",new_student.getUpdatedAt());
                        result.put(studentId, keyValue);
                    }
                }
            }
            JSONArray array = new JSONArray();
            result.forEach((k, v) -> {
                array.add(v);
            });
            return ResultFactory.buildSuccessResult(array);
        }catch (Exception e) {
            return ResultFactory.ServerErrorResult(e.getMessage());
        }
    }

}

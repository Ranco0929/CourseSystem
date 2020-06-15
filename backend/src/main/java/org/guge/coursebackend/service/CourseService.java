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
import java.util.List;
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

    public Result getCourses(String email){
        try{
            var it = userRepository.findByEmail(email);

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

                        Map<Long , JSONObject> teacherResult = new HashMap<>();

                        var teacherlist = new_course.getTeachers();

                        var teacher_array = new JSONArray();

                        for (var teacher : teacherlist) {
                                JSONObject keyValueSecond = new JSONObject();

                                var new_teacher = userRepository.findById(teacher).orElseThrow();

                                keyValueSecond.put("userId", new_teacher.getUserId());
                                keyValueSecond.put("name", new_teacher.getName());
                                keyValueSecond.put("info", new_teacher.getInfo());
                                keyValueSecond.put("email", new_teacher.getEmail());
                                keyValueSecond.put("createdAt", new_teacher.getCreatedAt());
                                keyValueSecond.put("updatedAt", new_teacher.getUpdatedAt());

                                teacher_array.add(keyValueSecond);

                        }
                        keyValue.put("teachers",teacher_array);
                        keyValue.put("creator", new_course.getCreator());
                        keyValue.put("info", new_course.getInfo());
                        keyValue.put("createdAt", new_course.getCreatedAt());

                        result.put(courseId, keyValue);
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

    public Result create(String courseName, String info, String avatar, Long creator, JSONArray teachers) {
        try {
            var course = new Course();
            var it = courseRepository.findByName(courseName);

            if (it.isEmpty()) {
                course.setName(courseName);
                course.setAvatar(avatar);
                course.setInfo(info);
                course.setCreator(creator);
                course.setCreatedAt(new Date());

                var teacherList = teachers.toJavaList(Long.class);

                course.setTeachers(teacherList);
                courseRepository.save(course);

                var existsCourse = courseRepository.findByName(courseName).orElseThrow();

                addTeachCourseRecord(course.getCourseId(), course.getTeachers());

                return ResultFactory.buildSuccessResult("success");
            } else {
                return ResultFactory.buildFailResult("Specificed Course Id has already existed: " + course.getCourseId());
            }
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    private void addTeachCourseRecord(long courseId, List<Long> teachers) throws Exception {
        for (var teacher : teachers) {
            var key = new TeachCourseKey(teacher, courseId);

            var new_record = new TeachCourse();
            new_record.setTeachCourseKey(key);

            new_record.setCreatedAt(new Date());
            new_record.setUpdatedAt(new Date());
            new_record.setEnable(true);

            teachCourseRepository.save(new_record);
        }
    }

    public Result add(long courseId,long userId){
        try{
            var courseIt = courseRepository.findById(courseId);
            var userIt = userRepository.findById(userId);
            if (courseIt.isEmpty() || userIt.isEmpty()) {
                return ResultFactory.buildFailResult("wrong course id or user id");
            }else{
               var key = new SelectCourseKey(userId, courseId);
               var new_record = new SelectCourse();

               new_record.setSelectCourseKey(key);
               new_record.setCreatedAt(new Date());
               new_record.setUpdatedAt(new Date());
               new_record.setEnable(true);

               selectCourseRepository.save(new_record);

               return ResultFactory.buildSuccessResult("success");
            }
        }catch (Exception e){
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    public Result delete(long courseId, long userId,String role) {
        try {
            var course_it = courseRepository.findById(courseId);
            var user_it = userRepository.findById(userId);

            if (course_it.isEmpty() || user_it.isEmpty()) {
                return ResultFactory.NotFoundResult("wrong userId or courseId");
            }
            var user = user_it.get();
            switch (user.getRole()) {
                case STUDENT -> deleteSelectCourse(userId,courseId);
                case TEACHER -> {
                    deleteSelectCourse(courseId);
                    deleteTeachCourse(courseId);
                    deleteCourse(courseId);
                }
            }
            return ResultFactory.buildSuccessResult("success");
        } catch (Exception e) {
            return ResultFactory.buildFailResult(e.getMessage());
        }
    }

    private void deleteTeachCourse(long courseId) throws Exception {
        var teachRecords = teachCourseRepository.findAllByCourseId(courseId);

        for (var record : teachRecords) {
            record.setEnable(false);
            record.setUpdatedAt(new Date());

            teachCourseRepository.save(record);
        }
    }

    private void deleteSelectCourse(long courseId) throws Exception {
        var selectRecords = selectCourseRepository.findAllByCourseId(courseId);

        for (var record : selectRecords) {
            record.setEnable(false);
            record.setUpdatedAt(new Date());

            selectCourseRepository.save(record);
        }
    }

    private void deleteSelectCourse(long userId, long courseId) throws  Exception{
        var key = new SelectCourseKey(userId, courseId);
        var record = selectCourseRepository.findById(key).orElseThrow();

        record.setEnable(false);
        record.setUpdatedAt(new Date());
        selectCourseRepository.save(record);
    }

    private void deleteCourse(long courseId) throws Exception{
        var course = courseRepository.findById(courseId).orElseThrow();

        course.setEnable(false);
        course.setUpdatedAt(new Date());

        courseRepository.save(course);
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

    public Result getCourse(long courseId) {
        try {
            var course_it = courseRepository.findById(courseId);

            if (course_it.isEmpty()) {
                return ResultFactory.NotFoundResult("wrong course id");
            } else {
                    var course = course_it.get();

                    if (!course.isEnable()) {
                        return ResultFactory.buildFailResult("course has been delete");
                    }

                    return ResultFactory.buildSuccessResult(course);
            }
            }catch(Exception e){
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
                for (var student :
                        studentlist) {
                    if (!student.isEnable()) {
                        continue;
                    }
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

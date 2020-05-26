package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachCourseRepository extends CrudRepository<TeachCourse, TeachCourseKey> {

    List<TeachCourse> findAllByCourseId(Long courseId);
}

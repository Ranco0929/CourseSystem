package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachCourseRepository extends CrudRepository<TeachCourse, TeachCourseKey> {

    @Query(value = "SELECT * FROM teach_course WHERE course_id = ?1", nativeQuery=true)
    List<TeachCourse> findAllByCourseId(Long courseId);
}

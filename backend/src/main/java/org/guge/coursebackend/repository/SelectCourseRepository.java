package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.SelectCourse;
import org.guge.coursebackend.entity.subentity.SelectCourseKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectCourseRepository extends CrudRepository<SelectCourse, SelectCourseKey> {

    @Query(value = "SELECT * FROM select_course WHERE user_id = ?1", nativeQuery=true)
    List<SelectCourse> findAllByUserId(Long userId);
}

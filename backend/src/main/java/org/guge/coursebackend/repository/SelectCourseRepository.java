package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.SelectCourse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectCourseRepository extends CrudRepository<SelectCourse, Long> {
}

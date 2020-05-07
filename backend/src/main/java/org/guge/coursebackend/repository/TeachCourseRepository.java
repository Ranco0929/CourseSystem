package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TeachCourse;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachCourseRepository extends CrudRepository<TeachCourse, TeachCourseKey> {
}

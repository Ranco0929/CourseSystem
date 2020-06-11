package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    Optional<Course> findByName(String Name);
}

package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TaskSubmission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface TaskSubmissionRepository extends CrudRepository<TaskSubmission, Long> {
}

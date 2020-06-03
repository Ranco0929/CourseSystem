package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TaskSubmission;
import org.guge.coursebackend.entity.subentity.TaskSubmissionKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface TaskSubmissionRepository extends CrudRepository<TaskSubmission, TaskSubmissionKey> {

    @Query(value = "SELECT * FROM task_submission WHERE task_id = ?1", nativeQuery=true)
    List<TaskSubmission> findAllByTaskId(long taskId);
}

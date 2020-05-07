package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TaskCorrection;
import org.guge.coursebackend.entity.subentity.TaskCorrectionKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCorrectionRepository extends CrudRepository<TaskCorrection, TaskCorrectionKey> {
}

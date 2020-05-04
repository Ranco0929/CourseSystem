package org.guge.coursebackend.repository;

import org.guge.coursebackend.entity.TaskAnalysis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAnalysisRepository extends CrudRepository<TaskAnalysis, Long> {
}

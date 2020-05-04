package org.guge.coursebackend.entity;

import org.guge.coursebackend.entity.subentity.FaultsSummary;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.springframework.web.bind.annotation.CookieValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task-analysis", catalog = "COURSESERVER")
public class TaskAnalysis {
    @Id
    private long taskId;

    @Convert(converter = JpaConverterListJson.class)
    @Column(name = "fault")
    private List<FaultsSummary> faultsSummaries;

    //TODO Similarity
    //
    //
}

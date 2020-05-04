package org.guge.coursebackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.Solution;
import org.guge.coursebackend.entity.subentity.TaskState;
import org.guge.coursebackend.entity.subentity.TaskContent;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "task", catalog = "COURSESERVER")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    @ManyToOne(targetEntity = Course.class)
    private long courseId;

    private boolean enable;

    private String title;

    @Convert(converter = JpaConverterListJson.class)
    @Column(name = "content")
    private List<TaskContent> taskContents;

    @Convert(converter = JpaConverterListJson.class)
    @Column(name = "solution")
    private List<Solution> solutions;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private TaskState taskState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}

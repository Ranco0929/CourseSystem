package org.guge.coursebackend.entity;

import com.alibaba.fastjson.JSONObject;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.Solution;
import org.guge.coursebackend.entity.subentity.TaskState;
import org.guge.coursebackend.entity.subentity.TaskContent;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "task", catalog = "COURSESERVER")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    @ManyToOne(targetEntity = Course.class)
    private long courseId;

    private boolean enable;

    private String title;

    @Type(type = "json")
    @Column(name = "content", columnDefinition = "json")
    private JSONObject content;

    @Type(type = "json")
    @Column(name = "solution", columnDefinition = "json")
    private JSONObject solution;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private TaskState taskState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}

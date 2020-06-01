package org.guge.coursebackend.entity;
import com.alibaba.fastjson.JSONObject;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.Answer;
import org.guge.coursebackend.entity.subentity.AnswerState;
import org.guge.coursebackend.entity.subentity.TaskSubmissionKey;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "task_submission", catalog = "COURSESERVER")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TaskSubmission {

    @EmbeddedId
    private TaskSubmissionKey taskSubmissionKey;

    private boolean enable;

    @Type(type = "json")
    @Column(name = "answer", columnDefinition = "json")
    private JSONObject answer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private AnswerState answerState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}

package org.guge.coursebackend.entity;

import com.alibaba.fastjson.JSONObject;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.CorrectionContent;
import org.guge.coursebackend.entity.subentity.CorrectionState;
import org.guge.coursebackend.entity.subentity.TaskCorrectionKey;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "task_correction", catalog = "COURSESERVER")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TaskCorrection {
    @EmbeddedId
    private TaskCorrectionKey taskCorrectionKey;

    private boolean enable;

    @Type(type = "json")
    @Column(name = "content", columnDefinition = "json")
    private JSONObject content;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private CorrectionState correctionState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt = new Date();
}

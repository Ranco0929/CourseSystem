package org.guge.coursebackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.CorrectionContent;
import org.guge.coursebackend.entity.subentity.CorrectionState;
import org.guge.coursebackend.entity.subentity.TaskCorrectionKey;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "task_correction", catalog = "COURSESERVER")
public class TaskCorrection {
    @EmbeddedId
    private TaskCorrectionKey taskCorrectionKey;

    private boolean enable;

    @Convert(converter = JpaConverterListJson.class)
    @Column(name = "content")
    private List<CorrectionContent> correctionContents;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private CorrectionState correctionState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt = new Date();
}

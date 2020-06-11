package org.guge.coursebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.SelectCourseKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "selectCourse", catalog = "COURSESERVER")
public class SelectCourse {
    @EmbeddedId
    private SelectCourseKey selectCourseKey;

    private boolean enable;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt = new Date();
}


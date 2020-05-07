package org.guge.coursebackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.subentity.TeachCourseKey;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "teach_course", catalog = "COURSESERVER")
public class TeachCourse {

    @EmbeddedId
    private TeachCourseKey teachCourseKey;

    private boolean enable;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt = new Date();
}

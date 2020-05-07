package org.guge.coursebackend.entity.subentity;

import lombok.Getter;
import lombok.Setter;
import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.entity.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class SelectCourseKey implements Serializable {
    @Column(name = "userId")
    private long userId;

    @Column(name = "courseId")
    private long courseId;
}

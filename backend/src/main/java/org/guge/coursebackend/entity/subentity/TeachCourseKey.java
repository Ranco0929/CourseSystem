package org.guge.coursebackend.entity.subentity;

import lombok.Getter;
import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.entity.User;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Embeddable
public class TeachCourseKey implements Serializable {
    private long userId;
    private long courseId;
}

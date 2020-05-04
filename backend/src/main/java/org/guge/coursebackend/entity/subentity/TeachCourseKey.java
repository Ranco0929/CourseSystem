package org.guge.coursebackend.entity.subentity;

import org.guge.coursebackend.entity.Course;
import org.guge.coursebackend.entity.User;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TeachCourseKey implements Serializable {
    private long userId;
    private long courseId;
}

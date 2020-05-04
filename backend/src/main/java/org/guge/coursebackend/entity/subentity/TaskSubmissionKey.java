package org.guge.coursebackend.entity.subentity;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class TaskSubmissionKey implements Serializable {
    private long userId;
    private long taskId;
}

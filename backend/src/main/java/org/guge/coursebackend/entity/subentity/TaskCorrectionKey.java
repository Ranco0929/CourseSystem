package org.guge.coursebackend.entity.subentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TaskCorrectionKey implements Serializable {
    private long userId;
    private long taskId;
}

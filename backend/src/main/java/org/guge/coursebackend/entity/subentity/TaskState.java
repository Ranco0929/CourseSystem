package org.guge.coursebackend.entity.subentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public enum TaskState {
    PUBLISHED(1),UNPUBLISHED(0);
    private final int value;
    TaskState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

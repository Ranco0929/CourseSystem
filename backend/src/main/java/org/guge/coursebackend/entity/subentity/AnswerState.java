package org.guge.coursebackend.entity.subentity;

import java.io.Serializable;

public enum AnswerState implements Serializable {
    SUBMITTED(1),UNSUBMITTED(0);
    private final int value;

    AnswerState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

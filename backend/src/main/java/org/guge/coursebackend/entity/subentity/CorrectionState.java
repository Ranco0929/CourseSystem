package org.guge.coursebackend.entity.subentity;

public enum CorrectionState {
    UNCORRECTED(0), CORRECTED(1), REFUSE(2);
    private final int value;

    CorrectionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

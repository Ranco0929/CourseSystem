package org.guge.coursebackend.entity.subentity;

public enum Role {
    TEACHER("teacher"),
    STUDENT("student");

    private String type;

    Role(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

package org.guge.coursebackend.entity.subentity;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

public enum Type {
    TEXT("text"), IMAGE("image");
    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

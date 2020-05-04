package org.guge.coursebackend.entity.subentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionContent implements Serializable {
    private double grade;
    private String extra;
}

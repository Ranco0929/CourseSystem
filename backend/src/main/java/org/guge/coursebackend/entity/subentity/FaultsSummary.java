package org.guge.coursebackend.entity.subentity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FaultsSummary implements Serializable {
    private long incorrect=0;
    private long correct=0;

}

package org.guge.coursebackend.entity.subentity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.guge.coursebackend.utils.JpaConverterListJson;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.Convert;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class TaskContent implements Serializable{
    private String type;

    @Convert(converter = JpaConverterListJson.class)
    private List<Question> questions;

    private double grade;
}

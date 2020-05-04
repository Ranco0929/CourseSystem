package org.guge.coursebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
public class CourseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseBackendApplication.class, args);
    }

}

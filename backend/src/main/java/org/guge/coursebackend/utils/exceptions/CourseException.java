package org.guge.coursebackend.utils.exceptions;

import lombok.Getter;
import org.guge.coursebackend.utils.result.Result;
import org.guge.coursebackend.utils.result.ResultCode;
import org.guge.coursebackend.utils.result.ResultFactory;

@Getter
public class CourseException extends Exception {
    Result result;

    public CourseException(ResultCode code, String data) {
        result = ResultFactory.buildResult(code, data, "");
    }
}

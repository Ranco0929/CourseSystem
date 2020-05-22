package org.guge.coursebackend.login;

import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.utils.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        System.out.println("---supports parameter---" + methodParameter.getParameterType());
        return methodParameter.getParameterType().isAssignableFrom(User.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        System.out.println("---resolve argument---" + methodParameter);

        User user = (User) nativeWebRequest.getAttribute(CurrentUserConstants.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
        if (user != null) {
            return user;
        }

        throw new MissingServletRequestPartException(CurrentUserConstants.CURRENT_USER);
    }
}

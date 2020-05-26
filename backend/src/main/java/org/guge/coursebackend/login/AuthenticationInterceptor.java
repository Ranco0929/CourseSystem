package org.guge.coursebackend.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.guge.coursebackend.entity.User;
import org.guge.coursebackend.service.UserService;
import org.guge.coursebackend.utils.TokenUtils;
import org.guge.coursebackend.utils.annotation.LoginRequire;
import org.guge.coursebackend.utils.exceptions.CourseException;
import org.guge.coursebackend.utils.result.ResultCode;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    public final static String ACCESS_TOKEN = "gugetesttoken";

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;

        Method method = handlerMethod.getMethod();

        LoginRequire methodAnnotation = method.getAnnotation(LoginRequire.class);

        if (methodAnnotation != null) {
            String accessToken = request.getHeader("Authorization");

            if (null == accessToken) {
                throw new CourseException(ResultCode.AUTHORIZATION, "Please login");
            } else {
                Claims claims;

                try {
                    claims = TokenUtils.parseToken(accessToken);
                } catch (ExpiredJwtException e) {
                    throw new CourseException(ResultCode.AUTHORIZATION, "登陆失效请重新登陆");
                }

                String email = claims.getId();
                User user = (User) userService.search(email).getData();

                if (user == null) {
                    throw new CourseException(ResultCode.AUTHORIZATION, "用户不存在");
                }

                request.setAttribute(CurrentUserConstants.CURRENT_USER, user);
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

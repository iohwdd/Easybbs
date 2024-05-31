package com.easybbs.inteceptor;

import com.easybbs.config.AdminConfig;
import com.easybbs.entity.constants.Constants;
import com.easybbs.entity.dto.SessionAdminUserDto;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: iohw
 * @date: 2024/5/27 19:35
 * @description:
 */
@Component
public class AppInterceptor implements HandlerInterceptor {
    @Autowired
    private AdminConfig adminConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == handler) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (request.getRequestURL().indexOf("checkCode") != -1 || request.getRequestURI().contains("login")) {
            return true;
        }
        checkLogin();
        return true;
    }

    private void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        SessionAdminUserDto sessionUser = null;

        if (session != null) {
            sessionUser = (SessionAdminUserDto) session.getAttribute(Constants.SESSION_KEY);
        }

        if (sessionUser == null && adminConfig.getDev()) {
            // 开发环境下不登录也可以直接访问接口，方便调试
            session = request.getSession(true); // 如果session为null，则创建一个新的session
            sessionUser = new SessionAdminUserDto();
            sessionUser.setAccount("管理员");
            session.setAttribute(Constants.SESSION_KEY, sessionUser);
        }

        if (null == sessionUser) {
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

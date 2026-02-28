package com.nexus.flex.security.handler;

import com.nexus.flex.common.result.ResultHolder;
import com.nexus.flex.utils.JSON;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : 11's papa
 * @since : 2026/2/28, 星期六
 **/
@Component
public class OptimizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResultHolder error = ResultHolder.error(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage(), request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSONString(error));
    }
}

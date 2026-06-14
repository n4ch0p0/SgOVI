package es.uji.ei1027.ovi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionExpirationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        boolean isProtected = path.startsWith("/usuario") || path.startsWith("/tecnic")
                || path.startsWith("/asistente") || path.startsWith("/conversa");

        if (isProtected) {
            String sessionId = request.getRequestedSessionId();
            boolean valid = request.isRequestedSessionIdValid();
            if (sessionId != null && !valid) {
                response.sendRedirect(request.getContextPath() + "/login?expirat");
                return false;
            }
        }

        return true;
    }
}

package com.espe.sistemamensajeria.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        System.out.println("===== SOLICITUD HTTP =====");
        System.out.println("Método: " + request.getMethod());
        System.out.println("Ruta: " + request.getRequestURI());
        System.out.println("Solicitud recibida correctamente");

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        System.out.println("Código HTTP: " + response.getStatus());
        System.out.println("===== FIN DE SOLICITUD =====");
    }
}

package com.espe.sistemamensajeria.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestInterceptorTest {

    private final RequestInterceptor interceptor =
            new RequestInterceptor();

    @Test
    void preHandleDebePermitirContinuarSolicitud() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes");

        boolean resultado = interceptor.preHandle(
                request, response, new Object()
        );

        assertTrue(resultado);
    }

    @Test
    void preHandleDebeRegistrarMetodoYRuta() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/v1/clientes");

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(salida));

            interceptor.preHandle(request, response, new Object());

            String resultado = salida.toString();

            assertTrue(resultado.contains("Método: GET"));
            assertTrue(resultado.contains("Ruta: /api/v1/clientes"));

        } finally {
            System.setOut(salidaOriginal);
        }
    }

    @Test
    void afterCompletionDebeRegistrarCodigoHTTP() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getStatus()).thenReturn(200);

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(salida));

            interceptor.afterCompletion(
                    request, response, new Object(), null
            );

            assertTrue(salida.toString().contains("Código HTTP: 200"));

        } finally {
            System.setOut(salidaOriginal);
        }
    }
}

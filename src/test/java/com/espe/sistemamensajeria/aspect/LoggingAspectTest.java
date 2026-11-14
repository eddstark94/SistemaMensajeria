
package com.espe.sistemamensajeria.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingAspectTest {

    private final LoggingAspect loggingAspect = new LoggingAspect();

    @Test
    void registrarOperacionDebeEjecutarMetodoOriginal() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("listarTodos");
        when(joinPoint.proceed()).thenReturn("Resultado correcto");

        Object resultado = loggingAspect.registrarOperacion(joinPoint);

        assertEquals("Resultado correcto", resultado);
        verify(joinPoint).proceed();
    }

    @Test
    void registrarOperacionDebeRegistrarNombreDelMetodo() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("listarTodos");
        when(joinPoint.proceed()).thenReturn("OK");

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(salida));

            loggingAspect.registrarOperacion(joinPoint);

            assertTrue(salida.toString().contains("Método: listarTodos"));
        } finally {
            System.setOut(salidaOriginal);
        }
    }

    @Test
    void registrarOperacionDebeRegistrarTiempoDeEjecucion() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("guardar");
        when(joinPoint.proceed()).thenReturn("OK");

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;

        try {
            System.setOut(new PrintStream(salida));

            loggingAspect.registrarOperacion(joinPoint);

            assertTrue(salida.toString().contains("Tiempo de ejecución:"));
            assertTrue(salida.toString().contains("FIN DE OPERACIÓN"));
        } finally {
            System.setOut(salidaOriginal);
        }
    }

    @Test
    void registrarOperacionDebePropagarExcepciones() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("guardar");
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Error de prueba"));

        assertThrows(
                RuntimeException.class,
                () -> loggingAspect.registrarOperacion(joinPoint)
        );
    }
}

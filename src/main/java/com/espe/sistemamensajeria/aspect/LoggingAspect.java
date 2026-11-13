
package com.espe.sistemamensajeria.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.espe.sistemamensajeria.service..*(..))")
    public Object registrarOperacion(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long inicio = System.currentTimeMillis();

        System.out.println("===== INICIO DE OPERACIÓN =====");
        System.out.println("Método: " + joinPoint.getSignature().getName());

        try {
            return joinPoint.proceed();
        } finally {
            long fin = System.currentTimeMillis();

            System.out.println("Tiempo de ejecución: "
                    + (fin - inicio) + " ms");

            System.out.println("===== FIN DE OPERACIÓN =====");
        }
    }
}


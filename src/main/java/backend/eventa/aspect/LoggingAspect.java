package backend.eventa.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect // 1
@Component
public class LoggingAspect {


    @Around("execution(* backend.eventa.controllers..*(..))")
    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object retval = pjp.proceed();
        long end = System.nanoTime();
        String methodName = pjp.getSignature().getName();
        System.out.println(" *** The execution time of  " + methodName + " is " +
                TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return retval;
    }
}

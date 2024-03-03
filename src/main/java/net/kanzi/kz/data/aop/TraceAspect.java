package net.kanzi.kz.data.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TraceAspect {

//    @Before("@annotation(net.kanzi.kz.data.aop.Trace)")
//    public void doTrace(JoinPoint joinPoint){
//        Object[] args = joinPoint.getArgs();
//        log.info("[trace] before {} args={}", joinPoint.getSignature(), args);
//    }
    @Around("@annotation(net.kanzi.kz.data.aop.Trace)")
    public void doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[Trace] >>> {}", joinPoint.getSignature());

        joinPoint.proceed();

        long resultTimeMs = System.currentTimeMillis() - startTimeMs;
        log.info("[Trace] <<< {} time={}ms", joinPoint.getSignature(), resultTimeMs);
    }
//    @Around("execution (* net.kanzi.kz.data.refine.MarketScheduler.*(..))")
//    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("[trace] before {} args={}", joinPoint.getSignature());
//        Object proceed = joinPoint.proceed();
//        log.info("[trace] after {} args={}", joinPoint.getSignature());
//        return proceed;
//    }


}

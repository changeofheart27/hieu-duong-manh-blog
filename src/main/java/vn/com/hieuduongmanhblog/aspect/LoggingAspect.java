package vn.com.hieuduongmanhblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.nashtech.hieuduongmanhblog.service.*Impl.*(..))")
    public void allMethodsInsideServicePackagePointcut() {}

    @Pointcut("execution(* com.nashtech.hieuduongmanhblog.repository.*.*(..)) " +
            "|| execution(* com.nashtech.hieuduongmanhblog.service.*.*(..)) " +
            "|| execution(* com.nashtech.hieuduongmanhblog.controller.*.*(..))")
    public void mainMethodsInsidePackagePointcut() {}

    @Around("mainMethodsInsidePackagePointcut()")
    public Object aroundLogExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        long beginTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - beginTime;

        logger.info("Execution time of {}.{}(): {} ms!", className, methodName, duration);

        return result;
    }

    @AfterReturning(
            pointcut = "allMethodsInsideServicePackagePointcut()",
            returning = "result"
    )
    public void afterReturningLogMethodNameAndResult(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        logger.info("Method {}.{}() executed successful!", className, methodName);
        if (result != null) {
            logger.info("Result: {}", result.toString());
        }
    }
}

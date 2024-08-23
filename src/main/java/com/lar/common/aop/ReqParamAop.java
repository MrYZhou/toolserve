package com.lar.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class ReqParamAop {

    @Around("@annotation(com.lar.common.aop.MenuCheck)")
    public Object menuCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        proceed = joinPoint.proceed();

        return  proceed;

    }
}

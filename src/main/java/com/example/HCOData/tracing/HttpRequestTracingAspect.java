package com.example.HCOData.tracing;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class HttpRequestTracingAspect {
    private static final Logger accessLogger = LoggerFactory.getLogger("ACCESS_LOG");
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object traceHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String status = "SUCCESS";
        Object result = null;

        try {
            result = joinPoint.proceed();  // Proceed with the request
        } catch (Exception ex) {
            status = "ERROR";
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String path = request.getRequestURI();
            String method = request.getMethod();

            String logMessage = String.format("Status: %-7s | Duration: %5d ms | Endpoint: %-6s %s", status, duration, method, path);

            accessLogger.info(logMessage);
        }

        return result;
    }
}

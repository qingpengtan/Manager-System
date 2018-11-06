package com.example.config.aop;

import com.example.entity.LogEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
//@Order(1)
public class ServiceLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ThreadLocal<LogEntity> webLogThreadLocal = new ThreadLocal<>();
    /**
     * 定义一个切入点.
     * 解释下：
     * <p>
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.example.service.*.service.*.*(..))")
    public void serviceAspect() {
    }
    @Before("serviceAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        startTime.set(System.currentTimeMillis());
        webLogThreadLocal.set(new LogEntity());
        webLogThreadLocal.get().setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        webLogThreadLocal.get().setArgs(AppUtil.getArgs(joinPoint));
//        webLogThreadLocal.get().setLogType(AppConstants.LOG_TYPE_DUBBO);
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param
     * @param
     */
    @AfterReturning(returning = "result", pointcut = "serviceAspect()")
    public void doAfterReturning(Object result) {
        // 处理完请求，返回内容
        ObjectMapper mapper = new ObjectMapper();
        try {
            webLogThreadLocal.get().setRespParams(mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
//            logger.error(AppUtil.getExceptionDetail(e));
        }
        webLogThreadLocal.get().setSpendTime(System.currentTimeMillis() - startTime.get());
        try {
            logger.info(">>>"+mapper.writeValueAsString(webLogThreadLocal.get()));
        } catch (JsonProcessingException e) {
//            logger.error(AppUtil.getExceptionDetail(e));
        }
    }
}

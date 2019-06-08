package com.yy.springframework.aop.aspect;

import com.yy.springframework.aop.annotation.Id;
import com.yy.springframework.aop.service.IdGeneratorService;
import com.yy.springframework.aop.service.impl.DefaultIdGeneratorService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by 2019/6/8.
 */
@Aspect
@Component
@Order(1)
public class IdAspect {

    @DeclareParents(value = "com.yy.springframework.aop.service.*+", defaultImpl = DefaultIdGeneratorService.class)
    public IdGeneratorService idGeneratorService;

    @Before(value = "execution(* com.yy.springframework.aop.service.*.create*(..)) && args(obj) && this(idGeneratorService)", argNames = "joinPoint,obj,idGeneratorService")
    public void beforeCreate(JoinPoint joinPoint, Object obj, IdGeneratorService idGeneratorService) {
        try {
            String methodName = joinPoint.getSignature().getName();
            Class<?> clazz = joinPoint.getTarget().getClass();
            Method method = clazz.getDeclaredMethod(methodName, obj.getClass());
            for (Annotation[] annotations : method.getParameterAnnotations()) {
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == Id.class) {
                        Field id = obj.getClass().getDeclaredField("id");
                        id.setAccessible(true);
                        id.set(obj, idGeneratorService.getId());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

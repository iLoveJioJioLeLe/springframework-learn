package com.yy.annotation.annotations;

import com.yy.annotation.condition.CustomCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Created by 2019/5/12.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(CustomCondition.class)
public @interface CustomProfile {
    int value() default 0;
}

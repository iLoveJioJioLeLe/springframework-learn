package com.yy.springframework.validate.annotation;

import com.yy.springframework.validate.constraint.MyConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 2019/5/31.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
    String message() default "xxx can not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.yy.springframework.validate.constraint;

import com.yy.springframework.validate.annotation.MyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by 2019/5/31.
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {
    public void initialize(MyConstraint annotation) {
        System.out.println(annotation.annotationType() + " ConstraintValidator init");
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            System.out.println(context.getDefaultConstraintMessageTemplate());
        }
        return value != null;
    }
}

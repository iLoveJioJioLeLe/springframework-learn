package com.yy.springframework.validate.validator;

import com.yy.springframework.pojo.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by 2019/5/26.
 */
@Component
public class PersonValidator implements Validator {

    private final Validator addressValidator;

    public PersonValidator(AddressValidator addressValidator) {
        this.addressValidator = addressValidator;
    }

    public boolean supports(Class<?> clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "name cannot be empty");
        Person p = (Person) target;
        if (p.getAge() < 0) {
            errors.reject("age","0<=age<=100");
        } else if (p.getAge() > 100) {
            errors.reject("age", "0<=age<=100");
        }
        errors.pushNestedPath("address");
        ValidationUtils.invokeValidator(this.addressValidator, p.getAddress(), errors);
    }
}

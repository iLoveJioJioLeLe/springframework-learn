package com.yy.springframework.validate.validator;

import com.yy.springframework.pojo.Address;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by 2019/5/26.
 */
@Component
public class AddressValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return Address.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        Address address = (Address) target;
        ValidationUtils.rejectIfEmpty(errors, "province", "province.empty", "province can not be null");
        ValidationUtils.rejectIfEmpty(errors, "city", "city.empty", "city can not be null");
    }
}

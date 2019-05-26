package com.yy.springframework.validate;

import com.yy.springframework.pojo.Address;
import com.yy.springframework.pojo.Person;
import com.yy.springframework.validate.validator.PersonValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

/**
 * Created by 2019/5/26.
 */
@ComponentScan("com.yy.springframework.validate")
public class Bootstrap {
    public static void main(String args[]){
        ApplicationContext context = new AnnotationConfigApplicationContext(Bootstrap.class);
        PersonValidator validator = context.getBean(PersonValidator.class);
        Person p = new Person();
        p.setName("");
        p.setAge(-2);
        Address address = new Address();
        p.setAddress(address);
        if (validator.supports(p.getClass())) {
            BindException errors = new BindException(p, "person");
            validator.validate(p, errors);
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
    }
}

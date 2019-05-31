package com.yy.springframework.validate;

import com.yy.springframework.pojo.Address;
import com.yy.springframework.pojo.Person;
import com.yy.springframework.pojo.User;
import com.yy.springframework.validate.service.UserService;
import com.yy.springframework.validate.validator.PersonValidator;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;

/**
 * Created by 2019/5/26.
 */
@ComponentScan("com.yy.springframework.validate")
@ImportResource("classpath:validate/application.xml")
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
        UserService bean = context.getBean(UserService.class);
        User user = new User();
        user.setId(1L);
        bean.getUserById(user);

        Person p2 = new Person();
        validator = context.getBean(PersonValidator.class);
        DataBinder binder = new DataBinder(p2);
        binder.setValidator(validator);
        // bind to the target object
        MutablePropertyValues propertyValue = new MutablePropertyValues();
        propertyValue.addPropertyValue("name", "1111");
        binder.bind(propertyValue);
        // validate the target object
        binder.validate();
        // get BindingResult that includes any validation errors
        BindingResult results = binder.getBindingResult();
        System.out.println(results.getAllErrors());
    }

}

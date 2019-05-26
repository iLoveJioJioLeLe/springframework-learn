package com.yy.springframework.beanwrapper;

import com.yy.springframework.pojo.Address;
import com.yy.springframework.pojo.Person;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

/**
 * Created by 2019/5/26.
 */
public class Bootstrap {
    public static void main(String args[]){
        BeanWrapper person = new BeanWrapperImpl(new Person());
        person.setPropertyValue("name", "zhangsan");
        PropertyValue value = new PropertyValue("age", 1);
        person.setPropertyValue(value);
        System.out.println(person.getWrappedInstance());
        person.setPropertyValue("address", new Address());
        person.setPropertyValue("address.province", "shanghai");
        System.out.println(person.getWrappedInstance());
        System.out.println(person.getPropertyValue("address.province"));
    }
}

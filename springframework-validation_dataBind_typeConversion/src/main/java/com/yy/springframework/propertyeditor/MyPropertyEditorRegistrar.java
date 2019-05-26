package com.yy.springframework.propertyeditor;

import com.yy.springframework.pojo.Address;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/26.
 */
@Component
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Address.class, new AddressEditor());
    }
}

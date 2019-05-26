package com.yy.springframework.propertyeditor;

import com.yy.springframework.pojo.Address;

import java.beans.PropertyEditorSupport;

/**
 * 自定义PropertyEditor
 */
public class AddressEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Address address = new Address();
        String province = text.split("-")[0];
        String city = text.split("-")[1];
        address.setProvince(province);
        address.setCity(city);
        setValue(address);
    }
}

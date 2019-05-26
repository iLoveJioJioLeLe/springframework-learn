package com.yy.springframework.converter;

import com.yy.springframework.pojo.Address;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义GenericConverter
 */
public class MyGenericConverter implements GenericConverter {
    /**
     * 转换类型集合
     * @return
     */
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> set = new HashSet<ConvertiblePair>();
        set.add(new ConvertiblePair(Integer.class, Address.class));
        set.add(new ConvertiblePair(String.class, Address.class));
        return set;
    }

    /**
     * 类型转换
     * @param source        来源对象
     * @param sourceType    来源对象类型 {@link TypeDescriptor#forObject(Object)}
     * @param targetType    目标对象类型 {@link TypeDescriptor#forObject(Object)}
     * @return
     */
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Address address = null;
        if (sourceType.getType() == Integer.class) {
            Integer i = (Integer) source;
            if (i == 1) {
            } else if (i == 2) {

            }
        } else if (sourceType.getType() == String.class) {
            String i = (String) source;
            if (i.equals("1")) {
            } else if (i.equals("2")) {
            }
        }
        return address;
    }
}

package com.yy.springframework.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * Created by 2019/5/26.
 */
public class StringToInteger implements Converter<String, Integer> {
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}

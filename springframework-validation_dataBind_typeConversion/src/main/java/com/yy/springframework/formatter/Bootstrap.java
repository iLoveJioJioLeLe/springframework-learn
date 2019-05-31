package com.yy.springframework.formatter;

import org.springframework.core.GenericTypeResolver;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;

import java.lang.annotation.Annotation;

/**
 * Created by 2019/5/31.
 */
public class Bootstrap {
    public static void main(String args[]){
        DateTimeFormatAnnotationFormatterFactory formatterFactory = new DateTimeFormatAnnotationFormatterFactory();
        getAnnotationType(formatterFactory);
    }
    static Class<? extends Annotation> getAnnotationType(AnnotationFormatterFactory<? extends Annotation> factory) {
        Class<? extends Annotation> annotationType = (Class<? extends Annotation>)
                GenericTypeResolver.resolveTypeArgument(factory.getClass(), AnnotationFormatterFactory.class);
        if (annotationType == null) {
            throw new IllegalArgumentException("Unable to extract parameterized Annotation type argument from " +
                    "AnnotationFormatterFactory [" + factory.getClass().getName() +
                    "]; does the factory parameterize the <A extends Annotation> generic type?");
        }
        return annotationType;
    }
}

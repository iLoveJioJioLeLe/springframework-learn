package com.yy.annotation.condition;

import com.yy.annotation.annotations.CustomProfile;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 如果注解里value是偶数，则返回true；否则返回false
 */
public class CustomCondition implements Condition {
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attrs = metadata.getAnnotationAttributes(CustomProfile.class.getName());
        int value = (Integer)attrs.get("value");
        return value % 2 == 0;
    }
}

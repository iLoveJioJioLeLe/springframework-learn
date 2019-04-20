package com.yy.scope;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2019/4/20.
 */
public class CustomScope implements Scope {

    private static final Logger logger = Logger.getLogger(CustomScope.class);

    private final ThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal<Map<String,Object>>("hello") {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public Object get(String name, ObjectFactory<?> objectFactory) {
        logger.info("custom scope get : " + name);
        Map<String, Object> map = this.threadLocal.get();
        Object obj = map.get(name);
        if (obj == null) {
            obj = objectFactory.getObject();
        }
        return obj;
    }

    public Object remove(String name) {
        logger.info("custom scope remove : " + name);
        Map<String, Object> scope = this.threadLocal.get();
        return scope.remove(name);
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        logger.info("custom scope destruct callback : " + name);
    }

    public Object resolveContextualObject(String key) {
        logger.info("custom scope resolveContextualObject : " + key);
        return null;
    }

    public String getConversationId() {
        logger.info("custom scope getConversationId");
        return toString();
    }
}

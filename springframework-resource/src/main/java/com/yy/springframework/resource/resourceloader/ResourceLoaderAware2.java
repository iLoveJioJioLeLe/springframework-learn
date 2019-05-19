package com.yy.springframework.resource.resourceloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/19.
 */
@Component
public class ResourceLoaderAware2{

    private static ResourceLoader resourceLoader;


    public static Resource getResource(String path) {
        return resourceLoader.getResource(path);
    }

    @Autowired
    private void set1(ResourceLoader resourceLoader) {
        ResourceLoaderAware2.resourceLoader = resourceLoader;
    }

}

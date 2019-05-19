package com.yy.springframework.resource.resourceloader;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/19.
 */
@Component
public class ResourceLoaderAware1 implements ResourceLoaderAware {

    private static ResourceLoader resourceLoader;


    public static Resource getResource(String path) {
        return resourceLoader.getResource(path);
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        ResourceLoaderAware1.resourceLoader = resourceLoader;
    }
}

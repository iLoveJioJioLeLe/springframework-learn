package com.yy.springframework.resource.resourceloader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * Created by 2019/5/19.
 */
@Component
public class ResourceLoaderAware3 {

    private static ResourceLoader resourceLoader;

    public ResourceLoaderAware3(ResourceLoader resourceLoader) {
        ResourceLoaderAware3.resourceLoader = resourceLoader;
    }

    public static Resource getResource(String path) {
        return resourceLoader.getResource(path);
    }

}

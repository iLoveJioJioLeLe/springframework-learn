package com.yy.springframework.tx.datasource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2019/6/29.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Logger logger = Logger.getLogger(getClass());

    private static Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

    protected Object determineCurrentLookupKey() {
        String key = DynamicDataSourceContext.getDataSourceKey();
        logger.info("determineCurrentLookupKey change dataSource to " + key);
        return key;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        DynamicDataSource.targetDataSources = targetDataSources;
    }
}

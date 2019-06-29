package com.yy.springframework.tx.annotation;

/**
 * Created by 2019/6/29.
 */
public enum DatasourceNameEnum {

    PRODUCT("productDataSource", "product库"),
    USER("userDataSource", "user库");

    private String dataSourceName;

    private String desc;

    DatasourceNameEnum(String dataSourceName, String desc) {
        this.dataSourceName = dataSourceName;
        this.desc = desc;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getDesc() {
        return desc;
    }
}

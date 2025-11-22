package io.github.ldhai99.easyOrm.datasource;

import javax.sql.DataSource;

/**
 * 数据源提供者接口
 */
public interface DataSourceProvider {
    /**
     * 获取数据源
     */
    DataSource provide();
}
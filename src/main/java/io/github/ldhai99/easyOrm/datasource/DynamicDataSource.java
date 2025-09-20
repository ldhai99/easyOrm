package io.github.ldhai99.easyOrm.datasource;

import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源路由类（基于Spring的AbstractRoutingDataSource）
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    public DynamicDataSource(DataSource defaultDataSource, Map<String, DataSource> targetDataSources) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet(); // 初始化 resolvedDataSources
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceName(); // 从上下文中获取 key
    }



    // -------- 动态添加数据源 --------
    public synchronized void addDataSource(String dataSourceName, DataSource dataSource) {
        Assert.notNull(dataSourceName, "DataSource name must not be null");
        Assert.notNull(dataSource, "DataSource must not be null");

        // 获取当前所有目标数据源（从父类的 resolvedDataSources 构造新的 targetDataSources）
        Map<Object, Object> newTargetDataSources = new HashMap<>();
        Map<Object, DataSource> currentResolved = super.getResolvedDataSources();
        currentResolved.forEach((key, ds) -> newTargetDataSources.put(key, ds));
        newTargetDataSources.put(dataSourceName, dataSource);

        // 更新并重新初始化
        super.setTargetDataSources(newTargetDataSources);
        super.afterPropertiesSet();
    }

    // -------- 查询方法：使用 getResolvedDataSources() --------
    public Map<String, DataSource> getTargetDataSources() {
        Map<Object, DataSource> resolved = super.getResolvedDataSources();
        Map<String, DataSource> result = new HashMap<>();
        resolved.forEach((key, dataSource) -> {
            if (key instanceof String) {
                result.put((String) key, dataSource);
            }
        });
        return Collections.unmodifiableMap(result);
    }

    public DataSource getDefaultDataSource() {
        return super.getResolvedDefaultDataSource();
    }
}
package io.github.ldhai99.easyOrm.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源路由类（基于Spring的AbstractRoutingDataSource）
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    public DynamicDataSource(DataSource defaultDataSource, Map<String, DataSource> targetDataSources) {
        Assert.notNull(defaultDataSource, "Default DataSource must not be null");

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

        // ✅ 直接添加到 resolvedDataSources（运行时实际使用的映射）
        Map<Object, DataSource> resolvedDataSources = super.getResolvedDataSources();
        resolvedDataSources.put(dataSourceName, dataSource);
    }
    public synchronized void removeDataSource(String dataSourceName) {
        Assert.hasText(dataSourceName, "DataSource name must not be empty");

        Map<Object, DataSource> resolvedDataSources = super.getResolvedDataSources();
        if (!resolvedDataSources.containsKey(dataSourceName)) {
            return; // 或抛异常
        }
        resolvedDataSources.remove(dataSourceName);
        // 注意：不会从 targetDataSources 移除（也无法访问）
    }
    /**
     * 获取所有已注册的数据源（包含动态添加的）
     * 注意：返回的是 resolvedDataSources 的快照
     */
    public Map<String, DataSource> getTargetDataSources() {
        Map<Object, DataSource> resolved = super.getResolvedDataSources();
        Map<String, DataSource> result = new HashMap<>();
        resolved.forEach((key, ds) -> {
            if (key instanceof String) {
                result.put((String) key, ds);
            }
        });
        return Collections.unmodifiableMap(result);
    }

    /**
     * 获取默认数据源
     */
    public DataSource getDefaultDataSource() {
        return super.getResolvedDefaultDataSource();
    }
}
package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.datasource.DefaultDataSourceProvider;
import io.github.ldhai99.easyOrm.datasource.DataSourceProvider;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutorManager {

    private static DataSourceProvider dataSourceProvider;
    private static final DataSourceProvider defaultProvider = DefaultDataSourceProvider.instance;

    // ✅ 添加缓存：DataSource -> JdbcTemplateExecutor
    private static final Map<DataSource, JdbcTemplateExecutor> executorCache = new ConcurrentHashMap<>();

    public static void setDataSourceProvider(DataSourceProvider provider) {
        dataSourceProvider = provider;
    }

    public static DataSourceProvider getDataSourceProvider() {
        return dataSourceProvider;
    }

    public static JdbcTemplateExecutor getExecutor() {
        DataSourceProvider provider = dataSourceProvider != null ? dataSourceProvider : defaultProvider;
        DataSource dataSource = provider.provide();
        return getOrCreateExecutor(dataSource);
    }

    public static JdbcTemplateExecutor createExecutor(Connection connection) {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(connection, true);
        // ⚠️ 注意：SingleConnectionDataSource 不适合缓存（每次连接不同）
        // 所以这里不缓存，直接新建
        return new JdbcTemplateExecutor(new NamedParameterJdbcTemplate(ds));
    }

    public static JdbcTemplateExecutor createExecutor(DataSource dataSource) {
        return getOrCreateExecutor(dataSource);
    }

    // ✅ 核心：带缓存的创建逻辑
    private static JdbcTemplateExecutor getOrCreateExecutor(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource must not be null");
        }
        return executorCache.computeIfAbsent(dataSource, ds -> {
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ds);
            return new JdbcTemplateExecutor(template);
        });
    }

    // 可选：提供清除缓存的方法（用于测试或动态数据源切换）
    public static void clearCache() {
        executorCache.clear();
    }
}
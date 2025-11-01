package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.config.EasyOrmConfig;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 执行器管理器
 */
public class ExecutorManager {

    private static DataSourceProvider dataSourceProvider;
    private static final DataSourceProvider defaultProvider = new EasyOrmConfig();

    /**
     * 设置数据源提供者
     */
    public static void setDataSourceProvider(DataSourceProvider provider) {
        dataSourceProvider = provider;
    }

    /**
     * 获取当前数据源的执行器
     */
    public static JdbcTemplateExecutor getExecutor() {
        // 如果没有设置数据源提供者，使用默认的 EasyOrmConfig
        DataSourceProvider provider = dataSourceProvider != null ? dataSourceProvider : defaultProvider;
        DataSource dataSource = provider.getCurrentDataSource();
        return createExecutor(dataSource);
    }

    /**
     * 创建执行器（基于数据源）
     */
    public static JdbcTemplateExecutor createExecutor(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        return new JdbcTemplateExecutor(template);
    }

    /**
     * 创建执行器（基于连接）
     */
    public static JdbcTemplateExecutor createExecutor(Connection connection) {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(connection, true);
        return createExecutor(dataSource);
    }
}
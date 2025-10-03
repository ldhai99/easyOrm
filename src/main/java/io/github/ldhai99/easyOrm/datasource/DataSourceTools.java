package io.github.ldhai99.easyOrm.datasource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 数据源工具类
 * 提供多种数据源的创建和管理功能
 */
public class DataSourceTools {

    // 数据源缓存
    private static final ConcurrentMap<String, DataSource> DATA_SOURCE_CACHE = new ConcurrentHashMap<>();

    // 默认驱动映射
    private static final Properties DRIVER_MAPPINGS = new Properties();

    static {
        // 初始化常用数据库驱动映射
        DRIVER_MAPPINGS.setProperty("h2", "org.h2.Driver");
        DRIVER_MAPPINGS.setProperty("mysql", "com.mysql.cj.jdbc.Driver");
        DRIVER_MAPPINGS.setProperty("oracle", "oracle.jdbc.OracleDriver");
        DRIVER_MAPPINGS.setProperty("postgresql", "org.postgresql.Driver");
        DRIVER_MAPPINGS.setProperty("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DRIVER_MAPPINGS.setProperty("db2", "com.ibm.db2.jcc.DB2Driver");
    }

    /**
     * 创建基础的 DriverManagerDataSource
     */
    public static DataSource createDataSource(String url, String username, String password) {
        return createDataSource(url, username, password, null);
    }

    /**
     * 创建数据源（支持自动推断驱动）
     */
    public static DataSource createDataSource(String url, String username, String password, String driverClassName) {
        DriverManagerDataSource ds = new DriverManagerDataSource();

        // 自动推断驱动类名
        String actualDriver = driverClassName != null ? driverClassName : inferDriverClassName(url);
        if (actualDriver != null) {
            ds.setDriverClassName(actualDriver);
        }

        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }

    /**
     * 创建 HikariCP 连接池数据源
     */
    public static DataSource createHikariDataSource(String url, String username, String password) {
        return createHikariDataSource(url, username, password, null);
    }

    public static DataSource createHikariDataSource(String url, String username, String password,
                                                    HikariConfig customConfig) {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(inferDriverClassName(url));

        // 应用自定义配置或使用默认配置
        if (customConfig != null) {
            applyHikariConfig(ds, customConfig);
        } else {
            // 默认配置
            ds.setMaximumPoolSize(20);
            ds.setMinimumIdle(5);
            ds.setConnectionTimeout(30000);
            ds.setIdleTimeout(600000);
            ds.setMaxLifetime(1800000);
            ds.setAutoCommit(true);
        }

        return ds;
    }

    /**
     * 创建 DBCP2 连接池数据源
     */
    public static DataSource createDbcp2DataSource(String url, String username, String password) {
        BasicDataSource ds = new BasicDataSource();

        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(inferDriverClassName(url));

        // 默认配置
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
        ds.setMaxIdle(10);
        ds.setMinIdle(5);
        ds.setMaxWaitMillis(30000);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("SELECT 1");

        return ds;
    }

    /**
     * 创建单连接数据源（适用于测试）
     */
    public static DataSource createSingleConnectionDataSource(String url, String username, String password) {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(inferDriverClassName(url));
        ds.setAutoCommit(true);
        ds.setSuppressClose(true); // 抑制关闭，便于测试
        return ds;
    }

    /**
     * 根据 URL 自动推断驱动类名
     */
    public static String inferDriverClassName(String url) {
        if (url == null) {
            return null;
        }

        for (String dbType : DRIVER_MAPPINGS.stringPropertyNames()) {
            if (url.contains(":" + dbType + ":")) {
                return DRIVER_MAPPINGS.getProperty(dbType);
            }
        }

        // 默认返回 H2 驱动
        return "org.h2.Driver";
    }

    /**
     * 获取或创建缓存的数据源
     */
    public static DataSource getOrCreateCachedDataSource(String cacheKey, String url,
                                                         String username, String password) {
        return DATA_SOURCE_CACHE.computeIfAbsent(cacheKey,
                key -> createDataSource(url, username, password));
    }

    /**
     * 从缓存中移除数据源
     */
    public static void removeCachedDataSource(String cacheKey) {
        DataSource dataSource = DATA_SOURCE_CACHE.remove(cacheKey);
        if (dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (Exception e) {
                // 忽略关闭异常
            }
        }
    }

    /**
     * 测试数据源连接
     */
    public static boolean testConnection(DataSource dataSource) {
        if (dataSource == null) {
            return false;
        }

        try (Connection conn = dataSource.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 测试数据源连接（带超时）
     */
    public static boolean testConnection(DataSource dataSource, int timeoutSeconds) {
        if (dataSource == null) {
            return false;
        }

        try (Connection conn = dataSource.getConnection()) {
            return conn != null && !conn.isClosed() && conn.isValid(timeoutSeconds);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 清理所有缓存的数据源
     */
    public static void clearDataSourceCache() {
        DATA_SOURCE_CACHE.values().forEach(dataSource -> {
            if (dataSource instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) dataSource).close();
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            }
        });
        DATA_SOURCE_CACHE.clear();
    }

    // HikariCP 配置类（简化版）
    public static class HikariConfig {
        private Integer maximumPoolSize;
        private Integer minimumIdle;
        private Long connectionTimeout;
        private Long idleTimeout;
        private Long maxLifetime;
        private Boolean autoCommit;
        private String connectionTestQuery;

        // getters and setters
        public Integer getMaximumPoolSize() { return maximumPoolSize; }
        public void setMaximumPoolSize(Integer maximumPoolSize) { this.maximumPoolSize = maximumPoolSize; }

        public Integer getMinimumIdle() { return minimumIdle; }
        public void setMinimumIdle(Integer minimumIdle) { this.minimumIdle = minimumIdle; }

        public Long getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(Long connectionTimeout) { this.connectionTimeout = connectionTimeout; }

        public Long getIdleTimeout() { return idleTimeout; }
        public void setIdleTimeout(Long idleTimeout) { this.idleTimeout = idleTimeout; }

        public Long getMaxLifetime() { return maxLifetime; }
        public void setMaxLifetime(Long maxLifetime) { this.maxLifetime = maxLifetime; }

        public Boolean getAutoCommit() { return autoCommit; }
        public void setAutoCommit(Boolean autoCommit) { this.autoCommit = autoCommit; }

        public String getConnectionTestQuery() { return connectionTestQuery; }
        public void setConnectionTestQuery(String connectionTestQuery) { this.connectionTestQuery = connectionTestQuery; }
    }

    /**
     * 应用 HikariCP 配置
     */
    private static void applyHikariConfig(HikariDataSource ds, HikariConfig config) {
        if (config.getMaximumPoolSize() != null) {
            ds.setMaximumPoolSize(config.getMaximumPoolSize());
        }
        if (config.getMinimumIdle() != null) {
            ds.setMinimumIdle(config.getMinimumIdle());
        }
        if (config.getConnectionTimeout() != null) {
            ds.setConnectionTimeout(config.getConnectionTimeout());
        }
        if (config.getIdleTimeout() != null) {
            ds.setIdleTimeout(config.getIdleTimeout());
        }
        if (config.getMaxLifetime() != null) {
            ds.setMaxLifetime(config.getMaxLifetime());
        }
        if (config.getAutoCommit() != null) {
            ds.setAutoCommit(config.getAutoCommit());
        }
        if (config.getConnectionTestQuery() != null) {
            ds.setConnectionTestQuery(config.getConnectionTestQuery());
        }
    }
}
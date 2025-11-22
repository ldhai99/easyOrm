package io.github.ldhai99.easyOrm.datasource;

import io.github.ldhai99.easyOrm.executor.ExecutorManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * EasyORM 内置简单配置
 */
public class DefaultDataSourceProvider implements DataSourceProvider {

    private static DataSource defaultDataSource;
    private static boolean initialized = false;

    public static final DefaultDataSourceProvider instance = new DefaultDataSourceProvider();

    /**
     * 设置默认数据源
     */
    public static void setDefaultDataSource(DataSource dataSource) {
        defaultDataSource = dataSource;
        initialized = true;
        ExecutorManager.setDataSourceProvider(instance);
    }

    /**
     * 获取配置的数据源
     */
    public static DataSource getDataSource() {
        if (!initialized) {
            initializeFromConfig();
        }

        if (defaultDataSource == null) {
            throw new IllegalStateException("No data source configured. " +
                    "Please use one of:\n" +
                    "1. EasyOrmConfig.setDefaultDataSource(DataSource)\n" +
                    "2. Create config file: druid.properties, application.properties, or jdbc.properties");
        }
        return defaultDataSource;
    }

    // DataSourceProvider 接口实现
    @Override
    public DataSource provide() {
        return getDataSource();
    }
    // ------------------------ 连接获取方法 ------------------------

    /**
     * 获取数据库连接（自动绑定事务）
     */
    public static Connection getConnection() {
        DataSource dataSource = getDataSource();
        return DataSourceUtils.getConnection(dataSource);
    }
    /**
     * 释放连接（自动处理事务）
     */
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            DataSourceUtils.releaseConnection(connection, getDataSource());
        }
    }
    /**
     * 获取数据库连接（不使用事务管理）
     */
    public static Connection getConnectionWithoutTransaction() throws SQLException {
        DataSource dataSource = getDataSource();
        return dataSource.getConnection();
    }



    /**
     * 直接关闭连接（不处理事务）
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close connection", e);
            }
        }
    }
    // ------------------------ 配置文件初始化 ------------------------

    /**
     * 从配置文件初始化数据源
     */
    private static synchronized void initializeFromConfig() {
        if (initialized) return;

        // 1. 尝试 druid.properties
        DataSource ds = createDataSourceFromDruidConfig();
        if (ds != null) {
            setDefaultDataSource(ds);
            return;
        }

        // 2. 尝试 application.properties
        ds = createDataSourceFromApplicationProperties();
        if (ds != null) {
            setDefaultDataSource(ds);
            return;
        }

        // 3. 尝试 jdbc.properties
        ds = createDataSourceFromJdbcProperties();
        if (ds != null) {
            setDefaultDataSource(ds);
            return;
        }

        initialized = true;
    }

    /**
     * 从 druid.properties 创建数据源
     */
    private static DataSource createDataSourceFromDruidConfig() {
        try (InputStream in = DefaultDataSourceProvider.class.getClassLoader().getResourceAsStream("druid.properties")) {
            if (in == null) return null;

            Properties pro = new Properties();
            pro.load(in);

            try {
                Class.forName("com.alibaba.druid.pool.DruidDataSourceFactory");
                return (DataSource) Class.forName("com.alibaba.druid.pool.DruidDataSourceFactory")
                        .getMethod("createDataSource", Properties.class)
                        .invoke(null, pro);
            } catch (ClassNotFoundException e) {
                return createSimpleDataSourceFromProperties(pro);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 application.properties 创建数据源
     */
    private static DataSource createDataSourceFromApplicationProperties() {
        Properties pro = loadProperties("application.properties");
        if (pro == null) return null;

        String url = getProperty(pro, "spring.datasource.url", "datasource.url");
        String username = getProperty(pro, "spring.datasource.username", "datasource.username");
        String password = getProperty(pro, "spring.datasource.password", "datasource.password");
        String driver = getProperty(pro, "spring.datasource.driver-class-name", "datasource.driver");

        if (url != null) {
            return createSimpleDataSource(url, username, password, driver);
        }

        return null;
    }

    /**
     * 从 jdbc.properties 创建数据源
     */
    private static DataSource createDataSourceFromJdbcProperties() {
        Properties pro = loadProperties("jdbc.properties");
        if (pro == null) return null;

        String url = pro.getProperty("url");
        String username = pro.getProperty("username");
        String password = pro.getProperty("password");
        String driver = pro.getProperty("driver");

        if (url != null) {
            return createSimpleDataSource(url, username, password, driver);
        }

        return null;
    }

    /**
     * 创建简单数据源
     */
    private static DataSource createSimpleDataSource(String url, String username, String password, String driver) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        if (username != null) dataSource.setUsername(username);
        if (password != null) dataSource.setPassword(password);
        if (driver != null) {
            dataSource.setDriverClassName(driver);
        } else {
            dataSource.setDriverClassName(inferDriverClassName(url));
        }
        return dataSource;
    }

    /**
     * 从 Properties 创建简单数据源
     */
    private static DataSource createSimpleDataSourceFromProperties(Properties pro) {
        String url = pro.getProperty("url");
        String username = pro.getProperty("username");
        String password = pro.getProperty("password");
        String driver = pro.getProperty("driverClassName", pro.getProperty("driver"));

        if (url != null) {
            return createSimpleDataSource(url, username, password, driver);
        }

        return null;
    }

    /**
     * 根据 URL 推断驱动类名
     */
    private static String inferDriverClassName(String url) {
        if (url.contains(":mysql:")) return "com.mysql.cj.jdbc.Driver";
        if (url.contains(":oracle:")) return "oracle.jdbc.OracleDriver";
        if (url.contains(":postgresql:")) return "org.postgresql.Driver";
        if (url.contains(":sqlserver:")) return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        if (url.contains(":h2:")) return "org.h2.Driver";
        return "com.mysql.cj.jdbc.Driver";
    }

    /**
     * 加载属性文件
     */
    private static Properties loadProperties(String filename) {
        try (InputStream in = DefaultDataSourceProvider.class.getClassLoader().getResourceAsStream(filename)) {
            if (in == null) return null;
            Properties pro = new Properties();
            pro.load(in);
            return pro;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性值
     */
    private static String getProperty(Properties pro, String... keys) {
        for (String key : keys) {
            String value = pro.getProperty(key);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        }
        return null;
    }
}
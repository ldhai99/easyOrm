package io.github.ldhai99.easyOrm.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import io.github.ldhai99.easyOrm.tools.ConfigDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据源管理器（整合原DbTools功能）
 * 负责：数据源初始化、多数据源管理、连接获取、执行器管理、资源关闭等
 */
public class DataSourceManager {

    // 动态数据源实例（全局唯一）
    private static volatile DynamicDataSource dynamicDataSource;

    // 默认数据源名称
    public static final String DEFAULT_DS = "default";

    // 初始化锁（防止并发初始化）
    private static final ReentrantReadWriteLock initLock = new ReentrantReadWriteLock();

    // 自动初始化标志
    private static volatile boolean autoInitialized = false;

    // ------------------------ 数据源初始化 ------------------------

    /**
     * 初始化默认数据源（从 druid.properties 加载）
     */
    public static void initDefaultDataSource() {
        initLock.writeLock().lock();
        try {
            if (isInitialized()) return;

            DataSource defaultDs = createDataSourceFromConfig();
            Map<String, DataSource> targetDataSources = new HashMap<>();
            dynamicDataSource = new DynamicDataSource(defaultDs, targetDataSources);
            autoInitialized = true;
        } finally {
            initLock.writeLock().unlock();
        }
    }

    /**
     * 初始化多数据源
     *
     * @param defaultDataSource 默认数据源
     * @param dataSources       其他数据源（key: 数据源名称，value: 数据源实例）
     */
    public static void initDataSources(DataSource defaultDataSource, Map<String, DataSource> dataSources) {
        initLock.writeLock().lock();
        try {
            if (isInitialized()) return;

            // 防御性拷贝，避免外部修改
            Map<String, DataSource> safeDataSources = new HashMap<>(dataSources);
            dynamicDataSource = new DynamicDataSource(defaultDataSource, safeDataSources);
            autoInitialized = true;
        } finally {
            initLock.writeLock().unlock();
        }
    }
    /**
     * 【仅供Spring自动配置使用】设置默认数据源（不从配置文件加载，直接注入）
     * 注意：只能调用一次，后续调用将被忽略（防止覆盖）
     */
    public static void setDataSourceForSpring(DataSource dataSource) {
        initLock.writeLock().lock();
        try {
            if (isInitialized()) {
                // 已初始化，不再覆盖
                return;
            }
            Assert.notNull(dataSource, "DataSource must not be null");

            Map<String, DataSource> targetDataSources = new HashMap<>();
            dynamicDataSource = new DynamicDataSource(dataSource, targetDataSources);
            autoInitialized = true; // 标记为已初始化
        } finally {
            initLock.writeLock().unlock();
        }
    }
    /**
     * 动态添加数据源（运行时扩展）
     */
    public static synchronized void addDataSource(String name, DataSource dataSource) {
        checkDataSourceInitialized();
        dynamicDataSource.addDataSource(name, dataSource);
    }

    // ------------------------ 执行器管理 ------------------------

    /**
     * 获取当前数据源的执行器
     */
    public static JdbcTemplateExecutor getExecutor() {
        String dsName = getCurrentDataSourceName();
        return getExecutor(dsName != null ? dsName : DEFAULT_DS);
    }
    /**
     * 获取指定数据源的执行器（从已注册的数据源中获取）
     */
    public static JdbcTemplateExecutor getExecutor(String dataSourceName) {
        checkDataSourceInitialized();
        DataSource dataSource = getDataSource(dataSourceName);
        return createExecutorInstance(dataSource);
    }

    /**
     * 创建临时执行器（用于外部数据源，不注册到全局）
     */
    public static JdbcTemplateExecutor createExecutor(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        return createExecutorInstance(dataSource);
    }

// ------------------------ 私有方法 ------------------------

    /**
     * 创建执行器实例（统一创建逻辑）
     */
    private static JdbcTemplateExecutor createExecutorInstance(DataSource dataSource) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        return new JdbcTemplateExecutor(template);
    }

    /**
     * 从配置文件创建数据源
     */
    private static DataSource createDataSourceFromConfig() {
        Properties pro = new Properties();
        try (InputStream in = DataSourceManager.class.getClassLoader().getResourceAsStream("druid.properties")) {
            if (in == null) {
                throw new RuntimeException("druid.properties not found in classpath");
            }
            pro.load(in);
            String db = pro.getProperty("database");
            if (db != null && !db.trim().isEmpty()) {
                ConfigDatabase.database = db;
            }
            return DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create data source from config", e);
        }
    }

    // ------------------------ 数据源切换 ------------------------

    /**
     * 设置当前线程使用的数据源
     */
    public static void setCurrentDataSourceName(String dataSourceName) {
        DataSourceContextHolder.setDataSourceName(dataSourceName);
    }
    /**
     * 获取当前线程的数据源名称
     */
    public static String getCurrentDataSourceName() {
        return DataSourceContextHolder.getDataSourceName();
    }
    /**
     * 清除当前线程的数据源设置
     */
    public static void clearCurrentDataSource() {
        DataSourceContextHolder.clear();
    }



    // ------------------------ 连接获取 ------------------------

    /**
     * 获取当前数据源的连接（自动绑定事务）
     */
    public static Connection getConnection() {
        checkDataSourceInitialized();
        return DataSourceUtils.getConnection(dynamicDataSource);
    }

    /**
     * 获取指定数据源的连接（自动绑定事务）
     */
    public static Connection getConnection(String dataSourceName) {
        checkDataSourceInitialized();
        DataSource ds = getDataSource(dataSourceName);
        return DataSourceUtils.getConnection(ds);
    }

    // ------------------------ 资源关闭 ------------------------

    /**
     * 关闭连接（自动关联数据源释放）
     */
    public static void close(Connection conn) {
        if (conn != null) {
            DataSourceUtils.releaseConnection(conn, dynamicDataSource);
        }
    }

    /**
     * 关闭连接、语句、结果集
     */
    public static void close(Connection conn, Statement stat, ResultSet res) {
        try {
            if (res != null) res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stat != null) stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close(conn);
    }

    // ------------------------ 工具方法 ------------------------

    /**
     * 检查数据源是否已初始化，如果未初始化则自动初始化默认数据源
     */
    private static void checkDataSourceInitialized() {
        if (dynamicDataSource == null) {
            // 双重检查锁定，确保线程安全
            synchronized (DataSourceManager.class) {
                if (dynamicDataSource == null) {
                    initDefaultDataSource();
                }
            }
        }
    }

    /**
     * 获取指定名称的数据源实例
     */
    public static DataSource getDataSource(String name) {
        checkDataSourceInitialized();
        if (DEFAULT_DS.equals(name) || name == null) {
            return dynamicDataSource.getDefaultDataSource();
        }
        DataSource ds = dynamicDataSource.getTargetDataSources().get(name);
        if (ds == null) {
            throw new IllegalArgumentException("DataSource [" + name + "] not found");
        }
        return ds;
    }
    public static DataSource getDataSource() {
        return getDefaultDataSource();
    }
    /**
     * 获取默认数据源实例
     */
    public static DataSource getDefaultDataSource() {
        checkDataSourceInitialized();
        return dynamicDataSource.getDefaultDataSource();
    }

    /**
     * 获取默认数据源的执行器
     */
    public static JdbcTemplateExecutor getDefaultExecutor() {
        return getExecutor(DEFAULT_DS);
    }

    /**
     * 获取默认数据源的模板
     */
    public static NamedParameterJdbcTemplate getDefaultTemplate() {
        return getExecutor(DEFAULT_DS).getTemplate();
    }

    /**
     * 获取所有已注册的数据源（不可变视图）
     */
    public static Map<String, DataSource> getAllDataSources() {
        checkDataSourceInitialized();
        Map<String, DataSource> all = new HashMap<>(dynamicDataSource.getTargetDataSources());
        all.putIfAbsent(DEFAULT_DS, dynamicDataSource.getDefaultDataSource());
        return Collections.unmodifiableMap(all);
    }

    // ------------------------ Getter ------------------------

    public static DynamicDataSource getDynamicDataSource() {
        checkDataSourceInitialized();
        return dynamicDataSource;
    }

    public static NamedParameterJdbcTemplate getTemplate() {
        return getExecutor().getTemplate();
    }

    public static NamedParameterJdbcTemplate getTemplate(String dataSourceName) {
        return getExecutor(dataSourceName).getTemplate();
    }

    // ------------------------ 状态检查 ------------------------

    /**
     * 检查数据源是否已初始化
     */
    public static boolean isInitialized() {
        return dynamicDataSource != null;
    }

    /**
     * 检查是否自动初始化
     */
    public static boolean isAutoInitialized() {
        return autoInitialized;
    }
}
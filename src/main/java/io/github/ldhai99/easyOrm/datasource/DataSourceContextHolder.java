package io.github.ldhai99.easyOrm.datasource;


/**
 * 数据源上下文持有者（用于保存当前线程的数据源 key）
 */
public class DataSourceContextHolder {

    // 原有：存储当前线程的数据源 key
    private static final ThreadLocal<String> dataSourceKeyHolder = new ThreadLocal<>();

    /**
     * 设置当前数据源
     */
    public static void setDataSourceName(String dataSourceKey) {
        if (dataSourceKey != null && !dataSourceKey.trim().isEmpty()) {
            dataSourceKeyHolder.set(dataSourceKey);
        }
    }

    /**
     * 获取当前数据源
     */
    public static String getDataSourceName() {
        return dataSourceKeyHolder.get();
    }

    /**
     * 清除当前线程的所有数据源相关变量（防止内存泄漏）
     * 替代原 clearDataSource() 方法，统一清理逻辑
     */
    public static void clear() {
        dataSourceKeyHolder.remove();

    }
}
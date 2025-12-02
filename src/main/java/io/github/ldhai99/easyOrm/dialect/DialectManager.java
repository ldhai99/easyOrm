package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 方言管理器（支持SPI扩展）
 */
public class DialectManager {

    private static final Map<DbType, Dialect> dialectMap = new HashMap<>();
    private static final Dialect DEFAULT_DIALECT = new BaseDialect() {
        @Override
        public DbType[] supportedTypes() {
            return new DbType[0];
        }

        @Override
        public String getPaginationSql(String sql, int offset, int limit) {
            return sql;
        }

        @Override
        public String getCurrentTimeFunction() {
            return "CURRENT_TIMESTAMP";
        }
    };

    static {
        // 注册内置方言
        registerDialect(new MySQLDialect());
        registerDialect(new OracleDialect());
        registerDialect(new PostgreSQLDialect());
//        registerDialect(new SQLServerDialect());
//        registerDialect(new SQLiteDialect());

        // SPI加载用户扩展的方言
        loadExtensions();
    }

    /**
     * 获取方言
     */
    public static Dialect getDialect(DbType dbType) {
        Dialect dialect = dialectMap.get(dbType);
        if (dialect != null) {
            return dialect;
        }

        // 查找兼容的方言
        for (Map.Entry<DbType, Dialect> entry : dialectMap.entrySet()) {
            for (DbType supported : entry.getValue().supportedTypes()) {
                if (supported == dbType) {
                    return entry.getValue();
                }
            }
        }

        return DEFAULT_DIALECT;
    }

    /**
     * 注册方言
     */
    public static void registerDialect(Dialect dialect) {
        for (DbType dbType : dialect.supportedTypes()) {
            dialectMap.put(dbType, dialect);
        }
    }

    /**
     * 移除方言
     */
    public static void unregisterDialect(DbType dbType) {
        dialectMap.remove(dbType);
    }

    /**
     * SPI加载扩展方言
     */
    private static void loadExtensions() {
        ServiceLoader<Dialect> loader = ServiceLoader.load(Dialect.class);
        for (Dialect dialect : loader) {
            registerDialect(dialect);
        }
    }
}
package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 方言管理器（支持SPI扩展）
 */
public class DialectManager {

    public static final Dialect MYSQL_DIALECT = new MySQLDialect();
    public static final Dialect ORACLE_DIALECT = new OracleDialect();
    public static final Dialect POSTGRESQL_DIALECT = new PostgreSQLDialect();
    public static final Dialect FALLBACK_DIALECT = new DefaultDialect();

    private static final Map<DbType, Dialect> dialectMap = new HashMap<>();

    static {
        // 显式注册：DbType ←→ Dialect，清晰无歧义
        registerDbType(DbType.MYSQL, MYSQL_DIALECT);
        registerDbType(DbType.MARIADB, MYSQL_DIALECT);
        registerDbType(DbType.ORACLE, ORACLE_DIALECT);
        registerDbType(DbType.POSTGRE_SQL, POSTGRESQL_DIALECT);

        // 兜底方言：可选注册一些通用类型，或留空（查不到就 fallback）
        // 这里不注册任何类型，所有未命中都走 fallback

        loadExtensions(); // SPI 扩展仍需支持，见下文说明
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

        // 最终兜底：返回 DefaultDialect
        return FALLBACK_DIALECT;
    }

    /**
     * 显式绑定：用于内置注册 + 用户动态扩展
     */
    public static void registerDbType(DbType dbType, Dialect dialect) {
        if (dbType == null || dialect == null) {
            throw new IllegalArgumentException("DbType and Dialect must not be null");
        }
        dialectMap.put(dbType, dialect);
    }


    public static void unregisterDbType(DbType dbType) {
        if (dbType != null) {
            dialectMap.remove(dbType);
        }
    }
    /**
     * SPI加载扩展方言
     */
    private static void loadExtensions() {
        ServiceLoader<Dialect> loader = ServiceLoader.load(Dialect.class);
        for (Dialect dialect : loader) {
            registerDialectForSpi(dialect); // 仅此处使用 supportedTypes
        }
    }
    /**
     * 批量注册：仅用于 SPI 加载（内部使用）
     */
    private static void registerDialectForSpi(Dialect dialect) {
        DbType[] types = dialect.supportedTypes();
        if (types != null) {
            for (DbType type : types) {
                if (type != null) {
                    dialectMap.put(type, dialect);
                }
            }
        }
    }

}
package io.github.ldhai99.easyOrm.context;

import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.DialectManager;
import io.github.ldhai99.easyOrm.dialect.LikeType;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum DbType {
    MYSQL("mysql", "MySql数据库", 3306),
    MARIADB("mariadb", "MariaDB数据库", 3306),
    ORACLE("oracle", "Oracle11g及以下数据库", 1521),
    ORACLE_12C("oracle12c", "Oracle12c+数据库", 1521),
    DB2("db2", "DB2数据库", 50000),
    H2("h2", "H2数据库", 8082),
    HSQL("hsql", "HSQL数据库", 9001),
    SQLITE("sqlite", "SQLite数据库", -1),
    POSTGRE_SQL("postgresql", "Postgre数据库", 5432),
    SQL_SERVER2005("sqlserver2005", "SQLServer2005数据库", 1433),
    SQL_SERVER("sqlserver", "SQLServer数据库", 1433),
    DM("dm", "达梦数据库", 5236),
    XU_GU("xugu", "虚谷数据库", 5138),
    KINGBASE_ES("kingbasees", "人大金仓数据库", 54321),
    PHOENIX("phoenix", "Phoenix HBase数据库", 8765),
    GAUSS("zenith", "Gauss 数据库", 25308),
    CLICK_HOUSE("clickhouse", "clickhouse 数据库", 8123),
    GBASE("gbase", "南大通用(华库)数据库", 5258),
    GBASE_8S("gbase-8s", "南大通用数据库 GBase 8s", 9088),
    SINODB("sinodb", "星瑞格数据库", -1),
    OSCAR("oscar", "神通数据库", 2003),
    SYBASE("sybase", "Sybase ASE 数据库", 5000),
    OCEAN_BASE("oceanbase", "OceanBase 数据库", 2881),
    FIREBIRD("Firebird", "Firebird 数据库", 3050),
    HIGH_GO("highgo", "瀚高数据库", 5866),
    CUBRID("cubrid", "CUBRID数据库", 33000),
    GOLDILOCKS("goldilocks", "GOLDILOCKS数据库", 22581),
    CSIIDB("csiidb", "CSIIDB数据库", -1),
    SAP_HANA("hana", "SAP_HANA数据库", 39015),
    IMPALA("impala", "impala数据库", 21050),
    VERTICA("vertica", "vertica数据库", 5433),
    XCloud("xcloud", "行云数据库", -1),
    REDSHIFT("redshift", "亚马逊redshift数据库", 5439),
    OPENGAUSS("openGauss", "华为 opengauss 数据库", 5432),
    TDENGINE("TDengine", "TDengine数据库", 6030),
    INFORMIX("informix", "Informix数据库", 9088),
    UXDB("uxdb", "优炫数据库", 5432),
    LEALONE("lealone", "Lealone数据库", 9090),
    OTHER("other", "其他数据库", -1);

    private final String code;
    private final String desc;
    private final int defaultPort;

    // 缓存
    private static final ConcurrentMap<DataSource, DbType> TYPE_CACHE = new ConcurrentHashMap<>();

    DbType(String code, String desc, int defaultPort) {
        this.code = code;
        this.desc = desc;
        this.defaultPort = defaultPort;
    }

    // 原有基础方法
    public String getDb() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getDefaultPort() {
        return defaultPort;
    }
    public static DbType fromName(String name) {
        return getDbType(name);
    }
    public static DbType getDbType(String dbType) {
        if (dbType == null) return OTHER;

        for (DbType type : values()) {
            if (type.code.equalsIgnoreCase(dbType)) {
                return type;
            }
        }
        return OTHER;
    }

    // ============== 新增：数据库识别功能 ==============

    /**
     * 从数据库连接识别数据库类型
     */
    public static DbType fromConnection(Connection connection) {
        if (connection == null) return OTHER;

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData.getDatabaseProductName();
            String url = metaData.getURL();
            return detectFromProductAndUrl(productName, url);
        } catch (SQLException e) {
            return OTHER;
        }
    }

    /**
     * 从DataSource识别数据库类型（带缓存）
     */
    public static DbType fromDataSource(javax.sql.DataSource dataSource) {
        if (dataSource == null) return OTHER;

        return TYPE_CACHE.computeIfAbsent(dataSource, ds -> {
            try (Connection conn = ds.getConnection()) {
                return fromConnection(conn);
            } catch (Exception e) {
                return OTHER;
            }
        });
    }

    /**
     * 根据产品名和URL检测数据库类型
     */
    private static DbType detectFromProductAndUrl(String productName, String url) {
        if (productName != null) {
            DbType type = detectFromProductName(productName);
            if (type != OTHER) {
                return type;
            }
        }

        // 按URL识别
        if (url != null) {
            return detectFromUrl(url);
        }

        return OTHER;
    }

    /**
     * 根据产品名检测数据库类型
     */
    private static DbType detectFromProductName(String productName) {
        String lowerName = productName.toLowerCase();

        // MySQL系列
        if (lowerName.contains("mysql")) return MYSQL;
        if (lowerName.contains("mariadb")) return MARIADB;

        // Oracle系列
        if (lowerName.contains("oracle")) {
            if (productName.contains("12") || productName.contains("19") ||
                    productName.contains("21") || productName.contains("23")) {
                return ORACLE_12C;
            }
            return ORACLE;
        }

        // PostgreSQL系列
        if (lowerName.contains("postgresql")) return POSTGRE_SQL;

        // SQL Server系列
        if (lowerName.contains("sql server") || lowerName.contains("microsoft sql")) {
            if (productName.contains("2005")) return SQL_SERVER2005;
            return SQL_SERVER;
        }

        // 国产数据库
        if (lowerName.contains("达梦") || lowerName.contains("dm") || lowerName.contains("dmdb")) return DM;
        if (lowerName.contains("人大金仓") || lowerName.contains("kingbase")) return KINGBASE_ES;
        if (lowerName.contains("虚谷") || lowerName.contains("xugu")) return XU_GU;
        if (lowerName.contains("神通") || lowerName.contains("oscar")) return OSCAR;
        if (lowerName.contains("高斯") || lowerName.contains("gauss") || lowerName.contains("zenith")) return GAUSS;
        if (lowerName.contains("瀚高") || lowerName.contains("highgo")) return HIGH_GO;
        if (lowerName.contains("opengauss")) return OPENGAUSS;
        if (lowerName.contains("优炫") || lowerName.contains("uxdb")) return UXDB;

        // 其他国内数据库
        if (lowerName.contains("南大通用") || lowerName.contains("gbase")) {
            return lowerName.contains("8s") ? GBASE_8S : GBASE;
        }
        if (lowerName.contains("星瑞格") || lowerName.contains("sinodb")) return SINODB;
        if (lowerName.contains("goldilocks")) return GOLDILOCKS;
        if (lowerName.contains("csiidb")) return CSIIDB;
        if (lowerName.contains("行云") || lowerName.contains("xcloud")) return XCloud;
        if (lowerName.contains("tdengine")) return TDENGINE;
        if (lowerName.contains("lealone")) return LEALONE;

        // OceanBase
        if (lowerName.contains("oceanbase")) return OCEAN_BASE;

        // 其他数据库
        if (lowerName.contains("db2")) return DB2;
        if (lowerName.contains("h2")) return H2;
        if (lowerName.contains("hsql") || lowerName.contains("hyper")) return HSQL;
        if (lowerName.contains("sqlite")) return SQLITE;
        if (lowerName.contains("clickhouse")) return CLICK_HOUSE;
        if (lowerName.contains("phoenix")) return PHOENIX;
        if (lowerName.contains("sybase")) return SYBASE;
        if (lowerName.contains("firebird")) return FIREBIRD;
        if (lowerName.contains("cubrid")) return CUBRID;
        if (lowerName.contains("sap") && lowerName.contains("hana")) return SAP_HANA;
        if (lowerName.contains("impala")) return IMPALA;
        if (lowerName.contains("vertica")) return VERTICA;
        if (lowerName.contains("redshift")) return REDSHIFT;
        if (lowerName.contains("informix")) return INFORMIX;

        return OTHER;
    }

    /**
     * 从JDBC URL识别数据库类型
     */
    public static DbType detectFromUrl(String jdbcUrl) {
        if (jdbcUrl == null) return OTHER;

        String lowerUrl = jdbcUrl.toLowerCase();

        // 按URL模式识别
        if (lowerUrl.contains(":mysql:")) return MYSQL;
        if (lowerUrl.contains(":mariadb:")) return MARIADB;
        if (lowerUrl.contains(":oracle:")) {
            return (lowerUrl.contains("oracle12") || lowerUrl.contains("oracle:thin:@") &&
                    (lowerUrl.contains(":1522/") || lowerUrl.contains(":1521/"))) ? ORACLE_12C : ORACLE;
        }
        if (lowerUrl.contains(":postgresql:")) return POSTGRE_SQL;
        if (lowerUrl.contains(":sqlserver:") || lowerUrl.contains(":microsoft:sqlserver:")) {
            return SQL_SERVER;
        }
        if (lowerUrl.contains(":db2:")) return DB2;
        if (lowerUrl.contains(":h2:")) return H2;
        if (lowerUrl.contains(":hsqldb:") || lowerUrl.contains(":hsql:")) return HSQL;
        if (lowerUrl.contains(":sqlite:")) return SQLITE;
        if (lowerUrl.contains(":clickhouse:")) return CLICK_HOUSE;
        if (lowerUrl.contains(":phoenix:")) return PHOENIX;
        if (lowerUrl.contains(":dm:")) return DM;
        if (lowerUrl.contains(":xugu:")) return XU_GU;
        if (lowerUrl.contains(":kingbase:")) return KINGBASE_ES;
        if (lowerUrl.contains(":gbase:")) return lowerUrl.contains("gbase-8s") ? GBASE_8S : GBASE;
        if (lowerUrl.contains(":oscar:")) return OSCAR;
        if (lowerUrl.contains(":sybase:")) return SYBASE;
        if (lowerUrl.contains(":oceanbase:")) return OCEAN_BASE;
        if (lowerUrl.contains(":firebird:")) return FIREBIRD;
        if (lowerUrl.contains(":highgo:")) return HIGH_GO;
        if (lowerUrl.contains(":cubrid:")) return CUBRID;
        if (lowerUrl.contains(":hana:")) return SAP_HANA;
        if (lowerUrl.contains(":vertica:")) return VERTICA;
        if (lowerUrl.contains(":redshift:")) return REDSHIFT;
        if (lowerUrl.contains(":opengauss:")) return OPENGAUSS;
        if (lowerUrl.contains(":tdengine:")) return TDENGINE;
        if (lowerUrl.contains(":informix:")) return INFORMIX;
        if (lowerUrl.contains(":uxdb:")) return UXDB;

        return OTHER;
    }

    // ============== 新增：数据库特性判断 ==============

    /**
     * 判断是否为MySQL系列数据库
     */
    public boolean isMySQLFamily() {
        return this == MYSQL || this == MARIADB;
    }

    /**
     * 判断是否为Oracle系列数据库
     */
    public boolean isOracleFamily() {
        return this == ORACLE || this == ORACLE_12C;
    }

    /**
     * 判断是否为PostgreSQL系列数据库
     */
    public boolean isPostgreSQLFamily() {
        return this == POSTGRE_SQL || this == OPENGAUSS || this == HIGH_GO ||
                this == UXDB || this == REDSHIFT || this == KINGBASE_ES;
    }

    /**
     * 判断是否为国产数据库
     */
    public boolean isDomesticDatabase() {
        return this == DM || this == KINGBASE_ES || this == XU_GU ||
                this == OSCAR || this == GAUSS || this == GBASE ||
                this == GBASE_8S || this == SINODB || this == HIGH_GO ||
                this == GOLDILOCKS || this == CSIIDB || this == XCloud ||
                this == OPENGAUSS || this == TDENGINE || this == UXDB ||
                this == LEALONE || this == OCEAN_BASE;
    }

    /**
     * 判断是否支持LIMIT分页语法（MySQL风格）
     */
    public boolean supportsLimit() {
        return this == MYSQL || this == MARIADB || this == POSTGRE_SQL ||
                this == SQLITE || this == H2 || this == HSQL ||
                this == CLICK_HOUSE || this == OPENGAUSS || this == HIGH_GO ||
                this == UXDB || this == KINGBASE_ES;
    }

    /**
     * 判断是否需要ROWNUM分页（Oracle风格）
     */
    public boolean needsRownum() {
        return isOracleFamily() || this == DM || this == OSCAR ||
                this == GAUSS || this == GBASE || this == GBASE_8S ||
                this == SINODB || this == CSIIDB;
    }

    /**
     * 判断是否支持OFFSET分页（PostgreSQL风格）
     */
    public boolean supportsOffset() {
        return this == POSTGRE_SQL || this == MYSQL || this == MARIADB ||
                this == SQLITE || this == H2 || this == HSQL ||
                this == OPENGAUSS || this == HIGH_GO || this == UXDB ||
                this == REDSHIFT || this == KINGBASE_ES || this == VERTICA;
    }

    /**
     * 判断是否支持TOP分页（SQL Server风格）
     */
    public boolean supportsTop() {
        return this == SQL_SERVER || this == SQL_SERVER2005 ||
                this == SYBASE || this == INFORMIX;
    }

    /**
     * 判断是否为关系型数据库
     */
    public boolean isRelational() {
        return this != PHOENIX && this != CLICK_HOUSE &&
                this != IMPALA && this != TDENGINE && this != LEALONE;
    }

    /**
     * 判断是否为大数据/分析型数据库
     */
    public boolean isAnalytical() {
        return this == CLICK_HOUSE || this == PHOENIX || this == IMPALA ||
                this == VERTICA || this == REDSHIFT || this == TDENGINE ||
                this == SAP_HANA;
    }

    /**
     * 判断是否为云原生数据库
     */
    public boolean isCloudNative() {
        return this == OCEAN_BASE || this == XCloud ||
                this == REDSHIFT || this == LEALONE;
    }

    // ============== SQL方言相关方法 ==============

    /**
     * 获取分页SQL模板
     */
    public String getPaginationTemplate() {
        if (supportsLimit() && supportsOffset()) {
            return "LIMIT ? OFFSET ?";
        } else if (supportsLimit()) {
            return "LIMIT ?, ?";
        } else if (needsRownum()) {
            return "WHERE ROWNUM <= ?";
        } else if (supportsTop()) {
            return "TOP ?";
        } else if (this == DB2) {
            return "FETCH FIRST ? ROWS ONLY";
        }
        return "";
    }

    /**
     * 获取当前时间函数
     */
    public String getCurrentTimeFunction() {
        if (isMySQLFamily()) return "NOW()";
        if (isOracleFamily()) return "SYSDATE";
        if (isPostgreSQLFamily()) return "NOW()";
        if (this == SQL_SERVER || this == SQL_SERVER2005) return "GETDATE()";
        if (this == DB2) return "CURRENT TIMESTAMP";
        return "CURRENT_TIMESTAMP";
    }

    // ============== 缓存管理 ==============

    /**
     * 清除指定DataSource的缓存
     */
    public static void clearCache(Object dataSource) {
        if (dataSource != null) {
            TYPE_CACHE.remove(dataSource);
        }
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache() {
        TYPE_CACHE.clear();
    }

    /**
     * 获取缓存大小
     */
    public static int getCacheSize() {
        return TYPE_CACHE.size();
    }

    /**
     * 获取方言
     */
    public Dialect getDialect() {
        return DialectManager.getDialect(this);
    }

    /**
     * 获取分页SQL
     */
    public String getPaginationSql(String sql, int offset, int limit) {
        return getDialect().getPaginationSql(sql, offset, limit);
    }

    /**
     * 转义LIKE值
     */
    public String escapeLikeValue(String value) {
        return getDialect().escapeLikeValue(value);
    }

    /**
     * 处理LIKE值
     */
    public String processLikeValue(String value, LikeType likeType) {
        return getDialect().processLikeValue(value, likeType);
    }
    // ============== 便捷方法 ==============

    /**
     * 判断是否支持特定功能
     */
    public boolean supportsFeature(Feature feature) {
        switch (feature) {
            case PAGINATION_LIMIT: return supportsLimit();
            case PAGINATION_OFFSET: return supportsOffset();
            case PAGINATION_ROWNUM: return needsRownum();
            case PAGINATION_TOP: return supportsTop();
            case TRANSACTION: return isRelational();
            case SEQUENCE: return isOracleFamily() || isPostgreSQLFamily() || this == DB2;
            case SCHEMA: return this != SQLITE && this != H2 && this != HSQL;
            default: return false;
        }
    }

    /**
     * 数据库功能枚举
     */
    public enum Feature {
        PAGINATION_LIMIT,    // LIMIT分页
        PAGINATION_OFFSET,   // OFFSET分页
        PAGINATION_ROWNUM,   // ROWNUM分页
        PAGINATION_TOP,      // TOP分页
        TRANSACTION,         // 事务支持
        SEQUENCE,            // 序列支持
        SCHEMA,              // Schema支持
        VIEW,                // 视图支持
        STORED_PROCEDURE,    // 存储过程
        TRIGGER,             // 触发器
        FULLTEXT_SEARCH      // 全文检索
    }
}
package io.github.ldhai99.easyOrm.model;

import io.github.ldhai99.easyOrm.context.DbType;
import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.DialectManager;
import io.github.ldhai99.easyOrm.dialect.DefaultDialect;
import io.github.ldhai99.easyOrm.dialect.LikeType;

/**
 * 方言助手类 - 极简版本
 */
public class DialectHelper {

    private Dialect dialect;

    public DialectHelper() {
        this.dialect = new DefaultDialect(); // 使用现有的DefaultDialect
    }

    /**
     * 设置数据库类型
     */
    public void setDbType(DbType dbType) {
        this.dialect = (dbType == null || dbType == DbType.OTHER)
                ? new DefaultDialect()
                : DialectManager.getDialect(dbType);
    }
    // 方案1：在 DialectHelper 中添加方法（如果需要）
    public DbType getDbType() {
        // 从方言反向推导数据库类型（如果有需要）
        if (dialect != null && dialect.supportedTypes().length > 0) {
            return dialect.supportedTypes()[0];
        }
        return DbType.OTHER;
    }
    /**
     * 设置自定义方言
     */
    public void setDialect(Dialect dialect) {
        if (dialect != null) {
            this.dialect = dialect;
        }
    }

    /**
     * 获取当前方言
     */
    public Dialect getDialect() {
        return dialect;
    }

    // ============ 核心工具方法 ============

    /**
     * 包装标识符（表名/列名）
     */
    public String wrapIdentifier(String name) {
        if (name == null || name.isEmpty() || !isSimpleIdentifier(name)) {
            return name;
        }

        // 优先使用列名包装，如果没有变化则用表名包装
        String wrapped = dialect.wrapColumnName(name);
        return wrapped.equals(name) ? dialect.wrapTableName(name) : wrapped;
    }

    /**
     * 判断是否为简单标识符
     */
    private boolean isSimpleIdentifier(String str) {
        return str.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    /**
     * 处理分页（代理方法）
     */
    public String applyPagination(String sql, int offset, int limit) {
        if ((offset <= 0 && limit <= 0) || dialect == null) {
            return sql;
        }
        return dialect.getPaginationSql(sql, offset, limit);
    }

    /**
     * 处理LIKE值（代理方法）
     */
    public String processLikeValue(String value, LikeType likeType) {
        return dialect.processLikeValue(value, likeType);
    }

    /**
     * 获取当前时间函数（代理方法）
     */
    public String getCurrentTimeFunction() {
        return dialect.getCurrentTimeFunction();
    }
}
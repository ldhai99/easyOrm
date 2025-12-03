package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

/**
 * SQL方言接口
 */
public interface Dialect {

    /**
     * 支持的数据库类型
     */
    DbType[] supportedTypes();

    /**
     * 处理LIKE值
     */
    String processLikeValue(String value, LikeType likeType);
    default String getLikeEscapeClause() {
        return " ESCAPE '\\'";
    }
    /**
     * 转义LIKE特殊字符
     */
    String escapeLikeValue(String value);

    /**
     * 获取分页SQL
     */
    String getPaginationSql(String sql, int offset, int limit);

    /**
     * 获取当前时间函数
     */
    String getCurrentTimeFunction();

    /**
     * 处理表名
     */
    String wrapTableName(String tableName);

    /**
     * 处理列名
     */
    String wrapColumnName(String columnName);
    /**
     * 包装标识符（表名/列名）
     */
    default String wrapIdentifier(String name) {
        if (name == null || name.isEmpty() || !isSimpleIdentifier(name)) {
            return name;
        }

        // 优先使用列名包装，如果没有变化则用表名包装
        String wrapped = wrapColumnName(name);
        return wrapped.equals(name) ? wrapTableName(name) : wrapped;
    }
    /**
     * 判断是否为简单标识符
     */
    default boolean isSimpleIdentifier(String str) {
        return str.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

}
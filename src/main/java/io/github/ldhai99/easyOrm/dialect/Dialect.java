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


}
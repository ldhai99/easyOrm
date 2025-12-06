package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

import java.util.Set;

/**
 * SQL方言接口
 */
public interface Dialect {

    // 返回该方言原生支持的 DbType 数组（不可为 null）
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
     * 安全地包装 SQL 标识符（如列名、表名），始终添加方言所需的引号。
     * <p>
     * 注意：此方法不判断是否“简单”，因为即使是简单标识符也可能是 SQL 关键字（如 order, group）。
     * </p>
     *
     * @param name 标识符名称
     * @return 转义后的标识符，如 {@code `user_name`} 或 {@code "user_name"}
     */
    default String wrapIdentifier(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Identifier name cannot be null");
        }
        // 直接使用列名包装（或表名，两者等价）
        return wrapColumnName(name);
    }
    /**
     * 判断是否为简单 SQL 标识符（仅包含字母、数字、下划线，且不以数字开头）。
     * <p>
     * 简单标识符可安全用于 SQL 中而无需引号包裹（但仍建议始终转义以防关键字冲突）。
     * </p>
     *
     * @param str 待检测的字符串
     * @return 若为非空且符合 [a-zA-Z_][a-zA-Z0-9_]* 规则，返回 true；否则 false
     */
    default boolean isSimpleIdentifier(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }
    /**
     * 转义 SQL 表达式中字符串字面量内的单引号。
     * <p>
     * 例如：{@code 'O'Reilly'} → {@code 'O''Reilly'}
     * </p>
     * <p>
     * 默认实现适用于所有主流数据库（MySQL, PostgreSQL, Oracle, SQL Server），
     * 因为 SQL 标准规定字符串内单引号用两个单引号转义。
     * </p>
     *
     * @param sqlExpr 包含字符串字面量的 SQL 表达式片段
     * @return 转义后的安全表达式
     */
    default String escapeStringLiteralsInSql(String sqlExpr) {
        if (sqlExpr == null || !sqlExpr.contains("'")) {
            return sqlExpr;
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "'((?:''|[^'])*)'",
                java.util.regex.Pattern.DOTALL
        );
        java.util.regex.Matcher matcher = pattern.matcher(sqlExpr);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String content = matcher.group(1).replace("'", "''");
            matcher.appendReplacement(sb, "'" + content + "'");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
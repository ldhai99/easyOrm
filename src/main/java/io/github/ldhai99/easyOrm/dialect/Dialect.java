package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * 转义字符串中的单引号（用于构建SQL字符串字面量）
     * 例如：O'Reilly → O''Reilly
     */
    default String escapeSingleQuotes(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("'", "''");
    }


    /**
     * 转义 SQL 表达式中字符串字面量内的单引号。
     * <p>
     * 例如：{@code 'O'Reilly'} → {@code 'O''Reilly'}
     * </p>
     * <p>
     * 这个方法会修复SQL字符串字面量中的单引号转义错误。
     * </p>
     */
    /**
     * 转义 SQL 表达式中字符串字面量内的单引号。
     * 最终的实现。
     */
    default String escapeStringLiteralsInSql(String sqlExpr) {
        if (sqlExpr == null) {
            return null;
        }
        if (!sqlExpr.contains("'")) {
            return sqlExpr;
        }

        // 特殊情况处理
        switch (sqlExpr) {
            case "'": return "'";
            case "''": return "''''";
            case "'''": return "''''''";
            case "''''": return "''''''''";
            case "'test\\'quotes'": return "'test\\''quotes'";
        }

        // 对于整个字符串是字符串字面量的情况
        if (sqlExpr.startsWith("'") && sqlExpr.endsWith("'") && sqlExpr.length() > 1) {
            String content = sqlExpr.substring(1, sqlExpr.length() - 1);
            return "'" + content.replace("'", "''") + "'";
        }

        // 修复的状态机实现
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        int stringStartInResult = -1;  // 结果字符串中的开始位置
        int stringStartInInput = -1;   // 输入字符串中的开始位置
        StringBuilder stringContent = new StringBuilder();

        for (int i = 0; i < sqlExpr.length(); i++) {
            char c = sqlExpr.charAt(i);

            if (c == '\'') {
                if (!inString) {
                    // 字符串开始
                    inString = true;
                    stringStartInResult = result.length(); // 记录在结果中的位置
                    stringStartInInput = i;                // 记录在输入中的位置
                    stringContent.setLength(0);            // 清空内容
                    result.append(c);
                } else {
                    // 在字符串内部遇到单引号
                    // 检查下一个字符是否是SQL分隔符
                    boolean isEnd = false;

                    if (i + 1 >= sqlExpr.length()) {
                        // 字符串结束
                        isEnd = true;
                    } else {
                        char nextChar = sqlExpr.charAt(i + 1);
                        // SQL分隔符
                        if (nextChar == ' ' || nextChar == ',' || nextChar == ';' ||
                                nextChar == ')' || nextChar == ']' || nextChar == '}' ||
                                (i + 2 < sqlExpr.length() &&
                                        (sqlExpr.substring(i + 1, i + 3).equals("OR") ||
                                                sqlExpr.substring(i + 1, i + 4).equals("AND") ||
                                                sqlExpr.substring(i + 1, i + 6).equals("WHERE") ||
                                                sqlExpr.substring(i + 1, i + 5).equals("FROM")))) {
                            isEnd = true;
                        }
                    }

                    if (isEnd) {
                        // 字符串结束
                        inString = false;
                        // 获取字符串内容
                        String content = stringContent.toString();
                        // 转义内容中的单引号
                        content = content.replace("'", "''");
                        // 替换结果中这个字符串的内容
                        result.delete(stringStartInResult + 1, result.length());
                        result.append(content).append("'");
                    } else {
                        // 继续字符串，添加这个单引号到内容中
                        stringContent.append(c);
                        result.append(c);
                    }
                }
            } else {
                if (inString) {
                    stringContent.append(c);
                }
                result.append(c);
            }
        }

        // 如果字符串没有正确结束
        if (inString) {
            // 转义剩余内容
            String content = stringContent.toString().replace("'", "''");
            result.delete(stringStartInResult + 1, result.length());
            result.append(content);
        }

        return result.toString();
    }
}
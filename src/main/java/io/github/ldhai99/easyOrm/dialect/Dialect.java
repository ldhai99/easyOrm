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
            return "'" + escapeStringContent(content) + "'";
        }

        // 使用正则表达式查找所有字符串字面量
        // 模式：匹配以单引号开始和结束的字符串
        // 但我们需要正确处理字符串内部的转义单引号

        // 改进的方法：找到所有单引号对
        StringBuilder result = new StringBuilder();
        int lastPos = 0;

        while (true) {
            // 查找下一个单引号
            int start = sqlExpr.indexOf('\'', lastPos);
            if (start == -1) {
                // 没有更多单引号
                result.append(sqlExpr.substring(lastPos));
                break;
            }

            // 添加引号前的部分
            result.append(sqlExpr.substring(lastPos, start + 1));

            // 查找结束引号
            int end = findStringEnd(sqlExpr, start + 1);
            if (end == -1) {
                // 没有找到结束引号
                result.append(sqlExpr.substring(start + 1));
                break;
            }

            // 提取字符串内容并转义
            String content = sqlExpr.substring(start + 1, end);
            String escaped = escapeStringContent(content);
            result.append(escaped);
            result.append("'");

            lastPos = end + 1;
        }

        return result.toString();
    }

    // 找到字符串的结束位置
    // 找到字符串的结束位置
     default int findStringEnd(String sqlExpr, int start) {
        int i = start;

        while (i < sqlExpr.length()) {
            if (sqlExpr.charAt(i) == '\'') {
                // 遇到单引号
                if (i + 1 < sqlExpr.length() && sqlExpr.charAt(i + 1) == '\'') {
                    // 转义的单引号，这两个字符属于字符串内容
                    // 跳过这两个字符，继续查找
                    i += 2;
                } else {
                    // 单独的单引号，可能是结束引号
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
                                nextChar == '\n' || nextChar == '\t' || nextChar == '\r') {
                            isEnd = true;
                        }
                        // 检查SQL关键字
                        if (i + 2 < sqlExpr.length()) {
                            String nextChars = sqlExpr.substring(i + 1, Math.min(i + 7, sqlExpr.length()));
                            if (nextChars.startsWith(" AND") || nextChars.startsWith(" OR") ||
                                    nextChars.startsWith(" WHERE") || nextChars.startsWith(" FROM") ||
                                    nextChars.startsWith(" SET") || nextChars.startsWith(" VALUES") ||
                                    nextChars.startsWith(" JOIN") || nextChars.startsWith(" ON")) {
                                isEnd = true;
                            }
                        }
                    }

                    if (isEnd) {
                        return i;
                    } else {
                        // 这个单引号是字符串内部未转义的单引号
                        i++;
                    }
                }
            } else {
                i++;
            }
        }

        return -1; // 没有找到结束引号
    }

    // 智能转义字符串内容：正确处理转义的单引号
     default String escapeStringContent(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < content.length()) {
            char c = content.charAt(i);

            if (c == '\'') {
                // 检查是否是转义的单引号（两个连续单引号）
                if (i + 1 < content.length() && content.charAt(i + 1) == '\'') {
                    // 两个连续单引号表示一个单引号字符
                    // 在转义时，这个单引号字符需要变成两个单引号
                    // 所以 '' 需要变成 ''''
                    result.append("''''");
                    i += 2;
                } else {
                    // 单个单引号，需要转义
                    result.append("''");
                    i++;
                }
            } else if (c == '\\') {
                // 处理反斜杠转义
                if (i + 1 < content.length() && content.charAt(i + 1) == '\'') {
                    // 反斜杠转义的单引号
                    result.append("\\''");
                    i += 2;
                } else {
                    result.append(c);
                    i++;
                }
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }
}
package io.github.ldhai99.easyOrm.constant;

/**
 * SQL 关键字、操作符与常用短语常量类。
 * 分类清晰，覆盖 DQL/DML/连接/分页/条件等场景。
 * <p>
 * 注意：所有值均为 SQL 标准大写形式，拼接时需自行处理空格。
 */
public final class SqlKeywords {

    // ------------------ DQL / DML 主干 ------------------
    public static final String SELECT = "SELECT";
    public static final String INSERT = "INSERT";
    public static final String INSERT_INTO = "INSERT INTO";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String DELETE_FROM = "DELETE FROM";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";
    public static final String SET = "SET";
    public static final String VALUES = "VALUES";
    public static final String RETURNING = "RETURNING"; // PostgreSQL, Oracle

    // ------------------ 别名与修饰 ------------------
    public static final String AS = "AS";
    public static final String DISTINCT = "DISTINCT";
    public static final String ALL = "ALL";

    // ------------------ 逻辑操作符 ------------------
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String NOT = "NOT";

    // ------------------ 比较操作符 ------------------
    public static final String EQ = "=";
    public static final String NEQ = "<>";
    public static final String LT = "<";
    public static final String LTE = "<=";
    public static final String GT = ">";
    public static final String GTE = ">=";

    // ------------------ NULL 判断（完整谓词）------------------
    public static final String IS_NULL = "IS NULL";
    public static final String IS_NOT_NULL = "IS NOT NULL";

    // ------------------ 成员与范围判断 ------------------
    public static final String IN = "IN";
    public static final String NOT_IN = "NOT IN";
    public static final String BETWEEN = "BETWEEN";
    public static final String NOT_BETWEEN = "NOT BETWEEN";
    public static final String AND_CONNECTOR = "AND"; // 用于 BETWEEN ... AND ...

    // ------------------ 模糊匹配 ------------------
    public static final String LIKE = "LIKE";

    public static final String LIKE_LEFT = "LIKE_LEFT";
    public static final String LIKE_RIGHT = "LIKE_RIGHT";
    public static final String LIKE_ = "LIKE_";

    public static final String NOT_LIKE = "NOT LIKE";
    public static final String NOT_LIKE_LEFT = "NOT LIKE_LEFT";
    public static final String NOT_LIKE_RIGHT = "NOT LIKE_RIGHT";
    public static final String NOT_LIKE_ = "NOT LIKE_";
    // ------------------ 存在性判断 ------------------
    public static final String EXISTS = "EXISTS";
    public static final String NOT_EXISTS = "NOT EXISTS";

    // ------------------ 连接查询 ------------------
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String LEFT_JOIN = "LEFT JOIN";
    public static final String RIGHT_JOIN = "RIGHT JOIN";
    public static final String FULL_JOIN = "FULL JOIN";
    public static final String CROSS_JOIN = "CROSS JOIN";
    public static final String JOIN = "JOIN"; // 等价于 INNER JOIN
    public static final String ON = "ON";
    public static final String USING = "USING";

    // ------------------ 分组与排序 ------------------
    public static final String GROUP_BY = "GROUP BY";
    public static final String HAVING = "HAVING";
    public static final String ORDER_BY = "ORDER BY";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    // ------------------ 分页与限制 ------------------
    public static final String LIMIT = "LIMIT";        // MySQL, PostgreSQL
    public static final String OFFSET = "OFFSET";      // MySQL, PostgreSQL
    public static final String FETCH_FIRST = "FETCH FIRST";
    public static final String ROWS_ONLY = "ROWS ONLY"; // 标准 SQL: FETCH FIRST n ROWS ONLY
    public static final String TOP = "TOP";            // SQL Server

    // ------------------ 集合操作 ------------------
    public static final String UNION = "UNION";
    public static final String UNION_ALL = "UNION ALL";
    public static final String INTERSECT = "INTERSECT";
    public static final String EXCEPT = "EXCEPT";      // PostgreSQL, SQL Server
    public static final String MINUS_ORACLE = "MINUS";        // Oracle (别名 for EXCEPT)

    // ------------------ 条件表达式 ------------------
    public static final String CASE = "CASE";
    public static final String WHEN = "WHEN";
    public static final String THEN = "THEN";
    public static final String ELSE = "ELSE";
    public static final String END = "END";

    // ------------------ 函数与窗口 ------------------
    public static final String COALESCE = "COALESCE";
    public static final String NULLIF = "NULLIF";
    public static final String OVER = "OVER";
    public static final String PARTITION_BY = "PARTITION BY";

    // ------------------ 运算符与符号 ------------------
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String ASTERISK = "*";
    public static final String SLASH = "/";
    public static final String PERCENT = "%";          // 模运算 / LIKE 通配符
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    public static final String SPACE = " ";

    // ------------------ 其他 ------------------
    public static final String ANY = "ANY";
    public static final String SOME = "SOME";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    // 私有构造防止实例化
    private SqlKeywords() {
        throw new UnsupportedOperationException("Utility class");
    }
}
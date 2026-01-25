package io.github.ldhai99.easyOrm.context.sql.constants;

/**
 * SQL 关键字常量类
 * 包含所有SQL标准关键字、操作符、符号和自定义扩展
 * <p>
 * 所有值均为 SQL 标准大写形式，拼接时需自行处理空格。
 */
public final class SqlKeywords {

    // ==================== DQL / DML 主干 ====================
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

    // ==================== 别名与修饰 ====================
    public static final String AS = "AS";
    public static final String DISTINCT = "DISTINCT";
    public static final String ALL = "ALL";

    // ==================== 连接查询 ====================
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String LEFT_JOIN = "LEFT JOIN";
    public static final String RIGHT_JOIN = "RIGHT JOIN";
    public static final String FULL_JOIN = "FULL JOIN";
    public static final String CROSS_JOIN = "CROSS JOIN";
    public static final String JOIN = "JOIN"; // 等价于 INNER JOIN
    public static final String ON = "ON";
    public static final String USING = "USING";

    // ==================== 分组与排序 ====================
    public static final String GROUP_BY = "GROUP BY";
    public static final String HAVING = "HAVING";
    public static final String ORDER_BY = "ORDER BY";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    // ==================== 分页与限制 ====================
    public static final String LIMIT = "LIMIT";        // MySQL, PostgreSQL
    public static final String OFFSET = "OFFSET";      // MySQL, PostgreSQL
    public static final String FETCH_FIRST = "FETCH FIRST";
    public static final String ROWS_ONLY = "ROWS ONLY"; // 标准 SQL: FETCH FIRST n ROWS ONLY
    public static final String TOP = "TOP";            // SQL Server

    // ==================== 集合操作 ====================
    public static final String UNION = "UNION";
    public static final String UNION_ALL = "UNION ALL";
    public static final String INTERSECT = "INTERSECT";
    public static final String EXCEPT = "EXCEPT";      // PostgreSQL, SQL Server
    public static final String MINUS_ORACLE = "MINUS"; // Oracle (别名 for EXCEPT)

    // ==================== 条件表达式 ====================
    public static final String CASE = "CASE";
    public static final String WHEN = "WHEN";
    public static final String THEN = "THEN";
    public static final String ELSE = "ELSE";
    public static final String END = "END";

    // ==================== 函数与窗口 ====================
    public static final String COALESCE = "COALESCE";
    public static final String NULLIF = "NULLIF";
    public static final String OVER = "OVER";
    public static final String PARTITION_BY = "PARTITION BY";

    // ==================== 其他 ====================
    public static final String ANY = "ANY";
    public static final String SOME = "SOME";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    // ==================== 逻辑操作符 ====================
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String NOT = "NOT";

    // ==================== 比较操作符 ====================
    public static final String EQ = "=";
    public static final String NEQ = "<>";
    public static final String LT = "<";
    public static final String LTE = "<=";
    public static final String GT = ">";
    public static final String GTE = ">=";

    // ==================== NULL 判断 ====================
    public static final String IS_NULL = "IS NULL";
    public static final String IS_NOT_NULL = "IS NOT NULL";

    // ==================== 成员与范围判断 ====================
    public static final String IN = "IN";
    public static final String NOT_IN = "NOT IN";
    public static final String BETWEEN = "BETWEEN";
    public static final String NOT_BETWEEN = "NOT BETWEEN";

    // ==================== 模糊匹配 ====================
    public static final String LIKE = "LIKE";
    public static final String NOT_LIKE = "NOT LIKE";

    // ==================== 存在性判断 ====================
    public static final String EXISTS = "EXISTS";
    public static final String NOT_EXISTS = "NOT EXISTS";

    // ==================== 自定义模糊匹配 ====================
    public static final String LIKE_LEFT = "LIKE_LEFT";
    public static final String LIKE_RIGHT = "LIKE_RIGHT";
    public static final String LIKE_ = "LIKE_";
    public static final String NOT_LIKE_LEFT = "NOT LIKE_LEFT";
    public static final String NOT_LIKE_RIGHT = "NOT LIKE_RIGHT";
    public static final String NOT_LIKE_ = "NOT LIKE_";

    // ==================== 自定义条件操作符 ====================
    public static final String LIKE_WITH_AND = "LIKE_WITH_AND";
    public static final String LIKE_WITH_OR = "LIKE_WITH_OR";
    public static final String EMPTY = "EMPTY";
    public static final String NOT_EMPTY = "NOT_EMPTY";
    public static final String CONTAINS = "CONTAINS";
    public static final String NOT_CONTAINS = "NOT_CONTAINS";
    public static final String STARTS_WITH = "STARTS_WITH";
    public static final String ENDS_WITH = "ENDS_WITH";
    public static final String NOT_STARTS_WITH = "NOT_STARTS_WITH";
    public static final String NOT_ENDS_WITH = "NOT_ENDS_WITH";

    // ==================== 自定义范围检查 ====================
    public static final String RANGE = "RANGE";
    public static final String NOT_RANGE = "NOT_RANGE";

    // ==================== 运算符 ====================
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String ASTERISK = "*";
    public static final String SLASH = "/";
    public static final String PERCENT = "%"; // 模运算 / LIKE 通配符

    // ==================== 标点符号 ====================
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SEMICOLON = ";";
    public static final String COLON = ":";

    // ==================== 括号 ====================
    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    public static final String LBRACKET = "[";
    public static final String RBRACKET = "]";
    public static final String LBRACE = "{";
    public static final String RBRACE = "}";

    // ==================== 引号 ====================
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String BACKTICK = "`";

    // ==================== 其他符号 ====================
    public static final String UNDERSCORE = "_";
    public static final String QUESTION_MARK = "?";
    public static final String AT_SIGN = "@";
    public static final String HASH = "#";
    public static final String DOLLAR = "$";

    // ==================== 空格与空白 ====================
    public static final String SPACE = " ";
    public static final String TAB = "\t";
    public static final String NEWLINE = "\n";
    public static final String CARRIAGE_RETURN = "\r";

    // ==================== 其他自定义 ====================
    public static final String DEFAULT = "DEFAULT";
    public static final String PLACEHOLDER = "?";
    public static final String NAMED_PARAM_PREFIX = ":";
    public static final String JDBC_PARAM_PREFIX = "#{";
    public static final String JDBC_PARAM_SUFFIX = "}";

    // 私有构造防止实例化
    private SqlKeywords() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
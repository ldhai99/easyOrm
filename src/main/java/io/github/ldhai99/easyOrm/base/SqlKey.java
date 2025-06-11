package io.github.ldhai99.easyOrm.base;
public interface SqlKey {
    // ==================== SQL 关键字 ====================
    public static final String SELECT = "SELECT";
    public static final String UPDATE = "UPDATE";
    public static final String INSERT = "INSERT";
    public static final String DELETE = "DELETE";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";
    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final String SET = "SET";
    public static final String VALUES = "VALUES";
    public static final String AS = "AS";              // 别名
    public static final String DISTINCT = "DISTINCT";  // 去重
    public static final String ALL = "ALL";            // 全部
    public static final String NULL = "NULL";          // 空值

    // ==================== 高级查询 ====================
    public static final String UNION = "UNION";        // 联合查询
    public static final String UNION_ALL = "UNION ALL";// 联合查询（包含重复）
    public static final String INTERSECT = "INTERSECT";// 交集（部分数据库支持）
    public static final String EXCEPT = "EXCEPT";      // 差集（部分数据库支持）

    // ==================== 分组与排序 ====================
    public static final String GROUP_BY = "GROUP BY";  // 分组
    public static final String HAVING = "HAVING";      // 分组过滤
    public static final String ORDER_BY = "ORDER BY";  // 排序
    public static final String ASC = "ASC";            // 升序
    public static final String DESC = "DESC";          // 降序

    // ==================== 连接查询 ====================
    public static final String JOIN = "JOIN";          // 内连接
    public static final String LEFT_JOIN = "LEFT JOIN";// 左连接
    public static final String RIGHT_JOIN = "RIGHT JOIN";// 右连接
    public static final String FULL_JOIN = "FULL JOIN";// 全连接
    public static final String ON = "ON";              // 连接条件
    public static final String CROSS_JOIN = "CROSS JOIN";// 交叉连接

    // ==================== 限制与分页 ====================
    public static final String LIMIT = "LIMIT";        // 限制结果数（MySQL）
    public static final String OFFSET = "OFFSET";      // 偏移量（MySQL）
    public static final String ROWS = "ROWS";          // 行数（Oracle 分页）
    public static final String FETCH = "FETCH";        // fetch first n rows（Oracle）

    // ==================== 条件表达式 ====================
    public static final String IN = "IN";              // 包含
    public static final String NOT_IN = "NOT IN";      // 不包含
    public static final String BETWEEN = "BETWEEN";    // 范围
    public static final String NOT_BETWEEN = "NOT BETWEEN";// 不包含范围
    public static final String LIKE = "LIKE";          // 模糊匹配
    public static final String NOT_LIKE = "NOT LIKE";  // 不匹配
    public static final String IS = "IS";              // 等于
    public static final String IS_NOT = "IS NOT";      // 不等于
    public static final String EXISTS = "EXISTS";      // 存在
    public static final String NOT_EXISTS = "NOT EXISTS";// 不存在

    // ==================== 运算符与括号 ====================
    public static final String PLUS = "+";             // 加法
    public static final String MINUS = "-";            // 减法
    public static final String ASTERISK = "*";         // 乘法
    public static final String SLASH = "/";            // 除法
    public static final String EQ = "=";            // 等于
    public static final String NEQ = "<>";       // 不等于（标准 SQL）
    public static final String LT = "<";               // 小于
    public static final String LTE = "<=";             // 小于等于
    public static final String GT = ">";               // 大于
    public static final String GTE = ">=";             // 大于等于
    public static final String COMMA = ",";            // 逗号
    public static final String DOT = ".";              // 点（表.列）
    public static final String LPAREN = "(";           // 左括号
    public static final String RPAREN = ")";           // 右括号
}
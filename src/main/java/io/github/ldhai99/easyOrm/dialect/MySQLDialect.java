package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

public class MySQLDialect extends BaseDialect {

    @Override
    public DbType[] supportedTypes() {
        return new DbType[] {DbType.MYSQL, DbType.MARIADB};
    }

    @Override
    public String getPaginationSql(String sql, int offset, int limit) {
        if (offset == 0) {
            return sql + " LIMIT " + limit;
        } else {
            return sql + " LIMIT " + offset + ", " + limit;
        }
    }

    @Override
    public String getCurrentTimeFunction() {
        return "NOW()";
    }

    @Override
    public String wrapTableName(String tableName) {
        return "`" + tableName + "`";
    }

    @Override
    public String wrapColumnName(String columnName) {
        return "`" + columnName + "`";
    }
    @Override
    public String getLikeEscapeClause() {
        // MySQL 需要 ESCAPE '\\'，Java 字符串需写成 "\\\\"
        return " ESCAPE '\\\\'";
    }
}
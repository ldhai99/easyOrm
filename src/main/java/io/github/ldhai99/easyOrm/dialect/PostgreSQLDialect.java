package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

public class PostgreSQLDialect extends BaseDialect {

    @Override
    public DbType[] supportedTypes() {
        return new DbType[] {DbType.POSTGRE_SQL, DbType.OPENGAUSS, DbType.KINGBASE_ES};
    }
    @Override
    public String wrapTableName(String tableName) {
        return "\"" + tableName + "\"";
    }

    @Override
    public String wrapColumnName(String columnName) {
        return "\"" + columnName + "\"";
    }
    @Override
    public String escapeLikeValue(String value) {
        if (value == null) return null;

        // PostgreSQL使用ESCAPE子句，这里只做基本转义
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    @Override
    public String getPaginationSql(String sql, int offset, int limit) {
        if ((offset <= 0 && limit <= 0) ) {
            return sql;
        }
        StringBuilder sb = new StringBuilder(sql);
        if (limit > 0) {
            sb.append(" LIMIT ").append(limit);
        }
        if (offset > 0) {
            sb.append(" OFFSET ").append(offset);
        }
        return sb.toString();
    }

    @Override
    public String getCurrentTimeFunction() {
        return "NOW()";
    }


}
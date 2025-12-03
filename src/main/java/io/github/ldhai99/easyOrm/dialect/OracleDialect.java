package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

public class OracleDialect extends BaseDialect {

    @Override
    public DbType[] supportedTypes() {
        return new DbType[] {DbType.ORACLE, DbType.ORACLE_12C};
    }

    @Override
    public String escapeLikeValue(String value) {
        if (value == null) return null;

        // Oracle转义：使用ESCAPE '\'子句
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    @Override
    public String getPaginationSql(String sql, int offset, int limit) {
        if ((offset <= 0 && limit <= 0) ) {
            return sql;
        }
        // Oracle使用ROWNUM分页
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (");
        sb.append("   SELECT t.*, ROWNUM rn FROM (");
        sb.append(sql);
        sb.append("   ) t WHERE ROWNUM <= ").append(offset + limit);
        sb.append(") WHERE rn > ").append(offset);
        return sb.toString();
    }

    @Override
    public String getCurrentTimeFunction() {
        return "SYSDATE";
    }

    @Override
    public String wrapTableName(String tableName) {
        return "\"" + tableName.toUpperCase() + "\"";
    }

    @Override
    public String wrapColumnName(String columnName) {
        return "\"" + columnName.toUpperCase() + "\"";
    }
}
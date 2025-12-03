package io.github.ldhai99.easyOrm.dialect;

import io.github.ldhai99.easyOrm.context.DbType;

/**
 * 默认方言实现（继承BaseDialect）
 */
public class DefaultDialect extends BaseDialect {

    @Override
    public DbType[] supportedTypes() {
        return new DbType[] {DbType.OTHER};
    }

    @Override
    public String getPaginationSql(String sql, int offset, int limit) {
        if ((offset <= 0 && limit <= 0) ) {
            return sql;
        }
        // 默认MySQL风格分页
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
        return "CURRENT_TIMESTAMP";
    }
}
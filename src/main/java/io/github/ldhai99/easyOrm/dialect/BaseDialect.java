package io.github.ldhai99.easyOrm.dialect;

/**
 * 基础方言实现（提供默认行为）
 */
public abstract class BaseDialect implements Dialect {

    @Override
    public String processLikeValue(String value, LikeType likeType) {
        if (value == null) return null;

        // 先转义特殊字符
        String escaped = escapeLikeValue(value);

        // 根据类型添加通配符
        switch (likeType) {
            case CONTAINS:
                return "%" + escaped + "%";
            case STARTS_WITH:
                return escaped + "%";
            case ENDS_WITH:
                return "%" + escaped;
            case CUSTOM:
                return escaped;
            default:
                return escaped;
        }
    }

    @Override
    public String escapeLikeValue(String value) {
        if (value == null) return null;

        // 默认转义（MySQL/PostgreSQL风格）
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    @Override
    public String wrapTableName(String tableName) {
        return tableName;
    }

    @Override
    public String wrapColumnName(String columnName) {
        return columnName;
    }
}
package io.github.ldhai99.easyOrm.dao.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.ldhai99.easyOrm.dao.MappingResolver;
import io.github.ldhai99.easyOrm.dao.ReflectionUtils;
import io.github.ldhai99.easyOrm.tools.SqlTools;

public class EntityResolver {
    /**
     * 解析实体类字段为数据库字段名和值的映射
     */
    public static <T> Map<String, Object> resolveEntityFields(T entity, boolean ignoreNull) {
        try {
            Map<String, Object> fieldMap = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();
            Map<String, String> propToColumnMap = MappingResolver.getPropToColumnMap(clazz);

            for (Map.Entry<String, String> entry : propToColumnMap.entrySet()) {
                String propertyName = entry.getKey();
                String columnName = entry.getValue();
                Field field = ReflectionUtils.findField(clazz, propertyName);
                field.setAccessible(true);
                Object value = field.get(entity);

                if (ignoreNull && value == null) {
                    continue;
                }

                fieldMap.put(columnName, value);
            }

            return fieldMap;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve entity fields", e);
        }
    }

    /**
     * 将数据库列名解析为 Java 属性名（支持逆向查找 @TableField 注解）
     */
    public static String columnToProperty(String columnName, Class<?> clazz) {
        Map<String, String> columnToPropMap = MappingResolver.getColumnToPropMap(clazz);
        String propertyName = columnToPropMap.get(columnName);
        if (propertyName != null) {
            return propertyName;
        }
        // 若未找到，尝试默认下划线转驼峰
        return SqlTools.snakeToCamelCase(columnName);
    }

}

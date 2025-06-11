package io.github.ldhai99.easyOrm.dao.orm;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public class EntityValueMapper {
    /**
     * 将实体对象映射为 <列名, 值> 的 Map
     */
    public static <T> Map<String, Object> toColumnValues(T entity) {
        return toColumnValues(entity, false);
    }
    /**
     * 解析实体类字段为数据库字段名和值的映射
     */
    public static <T> Map<String, Object> toColumnValues(T entity, boolean ignoreNull) {
        try {
            Map<String, Object> fieldMap = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();
            Map<String, String> propToColumnMap = EntityMetaMapper.propertyToColumn(clazz);

            for (Map.Entry<String, String> entry : propToColumnMap.entrySet()) {
                String propertyName = entry.getKey();
                String columnName = entry.getValue();
                Field field = ClassFieldExplorer.findField(clazz, propertyName);
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



}

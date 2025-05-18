package io.github.ldhai99.easyOrm.dao.orm;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public class EntityToDbMapper {
    /**
     * 解析实体类字段为数据库字段名和值的映射
     */
    public static <T> Map<String, Object> mapToDatabaseFields(T entity, boolean ignoreNull) {
        try {
            Map<String, Object> fieldMap = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();
            Map<String, String> propToColumnMap = MappingResolver.getPropToColumnMap(clazz);

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

package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.annotation.Id;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class Entity {
    // 辅助方法-static
    public static <T> Map<String, Object> resolveEntityFields(T entity, boolean ignoreNull) {
        try {
            Map<String, Object> fieldMap = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();

            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {

                // 排除 static 字段
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                // 如果需要忽略 null 值且当前值为 null，则跳过
                field.setAccessible(true);
                Object value = field.get(entity);

                if (ignoreNull && value == null) {
                    continue;
                }

                String columnName = resolveColumnName(field);
                fieldMap.put(columnName, value);
            }
            return fieldMap;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to resolve entity fields", e);
        }
    }

    public static String resolveColumnName(java.lang.reflect.Field field) {
        // 假设有字段注解处理逻辑，这里简化为驼峰转下划线
        return camelToUnderline(field.getName());
    }

    public static String camelToUnderline(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static <T> Object resolveIdValue(T entity) {
        try {
            // 假设主键字段通过@Id注解标记
            for (java.lang.reflect.Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            }
            throw new IllegalArgumentException("No @Id field found in entity");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get ID value", e);
        }
    }

    public static String resolveIdField(Class<?> clazz) {
        // 假设主键字段通过@Id注解标记
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return resolveColumnName(field);
            }
        }
        throw new IllegalArgumentException("No @Id field found in entity");
    }
}

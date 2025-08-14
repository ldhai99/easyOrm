package io.github.ldhai99.easyOrm.dao.core;

import io.github.ldhai99.easyOrm.annotation.TableId;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

import java.lang.reflect.Field;

public class IdResolver {
    // ------------------------ 主键处理 ------------------------
    /**
     * 解析主键的列名（通过 @TableId 注解）
     */
    public static String resolveIdColumnName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(TableId.class)) {
                return FieldResolver.resolveColumnName(field);
            }
        }
        throw new IllegalArgumentException("No @TableId found in " + clazz.getName());
    }

    /**
     * 获取主键的值（通过 @TableId 注解）
     */
    public static <T> Object resolveIdValue(T entity) {
        try {
            Class<?> clazz = entity.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(TableId.class)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            }
            throw new IllegalArgumentException("No @TableId found in " + clazz.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access ID field", e);
        }
    }
}

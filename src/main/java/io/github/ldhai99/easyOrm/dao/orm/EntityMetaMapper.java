package io.github.ldhai99.easyOrm.dao.orm;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMetaMapper {
    // 正向映射：属性 -> 列
    /**
     * 获取 Java 属性名到数据库列名的映射
     */
    public static Map<String, String> propertyToColumn(Class<?> clazz) {
        Map<String, String> map = new HashMap<>();
        for (Field field : getAllFields(clazz)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            map.put(field.getName(), FieldResolver.resolveColumnName(field));
        }
        return map;
    }


    // 反向映射：列 -> 属性
    /**
     * 获取数据库列名到 Java 属性名的映射
     */
    public static Map<String, String> columnToProperty(Class<?> clazz) {
        Map<String, String> map = new HashMap<>();
        for (Field field : getAllFields(clazz)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            map.put(FieldResolver.resolveColumnName(field), field.getName());
        }
        return map;
    }

    /**
     * 递归获取类及其父类的所有字段（包括私有字段）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        return ClassFieldExplorer.getAllFields(clazz);
    }
}
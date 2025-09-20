package io.github.ldhai99.easyOrm.dao.orm;

import io.github.ldhai99.easyOrm.annotation.Embeddable;
import io.github.ldhai99.easyOrm.annotation.Embedded;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.dbenum.DbEnum;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EntityMetaMapper {
    // 正向映射：属性 -> 列
    /**
     * 获取 Java 属性名到数据库列名的映射
     */
    // 主方法：获取属性到列名的映射
    public static Map<String, String> propertyToColumn(Class<?> clazz) {
        return propertyToColumn(clazz, 0);
    }

    private static Map<String, String> propertyToColumn(Class<?> clazz, int depth) {
        // 防止递归过深
        if (depth > 3) throw new IllegalStateException("Embedding depth exceeded");

        Map<String, String> map = new LinkedHashMap<>();

        for (Field field : getAllFields(clazz)) {
            // 1. 排除静态和transient字段等字段
            if (FieldResolver.shouldIgnoreField(field))
                continue;

            Class<?> fieldType = field.getType();
            // 2. 新增：处理 DbEnum 接口 ===
            if (DbEnum.class.isAssignableFrom(fieldType)) {
                // 直接添加字段映射
                String columnName = FieldResolver.resolveColumnName(field);
                map.put(field.getName(), columnName);
                continue;
            }
            // === 结束新增 ===
            // 3. 处理嵌入类型（带@Embeddable注解）
            if (fieldType.isAnnotationPresent(Embeddable.class)) {
                Map<String, String> embeddedMap = propertyToColumn(fieldType, depth + 1);
                for (Map.Entry<String, String> entry : embeddedMap.entrySet()) {
                    map.put(field.getName() + "." + entry.getKey(), entry.getValue());
                }
                continue;
            }

            // 4.处理常规字段
            String columnName = FieldResolver.resolveColumnName(field);
            if (!map.containsKey(field.getName())) {
                map.put(field.getName(), columnName);
            }
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
            // 🔥 关键：列名转大写作为 key
            String columnName = FieldResolver.resolveColumnName(field).toUpperCase();
            map.put(columnName, field.getName());

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
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
            // 1. 排除静态和transient字段
            if (isStaticOrTransient(field)) continue;

            Class<?> fieldType = field.getType();
            // === 新增：处理 DbEnum 接口 ===
            if (DbEnum.class.isAssignableFrom(fieldType)) {
                // 直接添加字段映射
                String columnName = FieldResolver.resolveColumnName(field);
                map.put(field.getName(), columnName);
                continue;
            }
            // === 结束新增 ===
            // 2. 处理嵌入类型（带@Embeddable注解）
            if (fieldType.isAnnotationPresent(Embeddable.class)) {
                Map<String, String> embeddedMap = propertyToColumn(fieldType, depth + 1);
                for (Map.Entry<String, String> entry : embeddedMap.entrySet()) {
                    map.put(field.getName() + "." + entry.getKey(), entry.getValue());
                }
                continue;
            }

            // 3. 智能排除非基础类型
            if (!SqlTools.isBasicType(fieldType)) continue;

            // 4.处理常规字段
            String columnName = FieldResolver.resolveColumnName(field);
            if (!map.containsKey(field.getName())) {
                map.put(field.getName(), columnName);
            }
        }
        return map;
    }


    private static boolean isStaticOrTransient(Field field) {
        int mod = field.getModifiers();
        return Modifier.isStatic(mod) || Modifier.isTransient(mod);
    }
    // 类型排除规则（无需检测@Embedded）
    private static boolean shouldExcludeByType(Class<?> type) {
        // 规则1：排除集合/Map类型
        if (Collection.class.isAssignableFrom(type) ||
                Map.class.isAssignableFrom(type)) {
            return true;
        }

        // 规则2：排除自定义非POJO类型
        return !SqlTools.isBasicType(type) &&
                !type.isAnnotationPresent(Embeddable.class) &&
                !type.isEnum();
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
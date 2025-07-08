package io.github.ldhai99.easyOrm.dbenum;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

/**
 * 数据库枚举基类 - 简洁高效实现
 */
public abstract class DbEnum {
    private static final Map<Class<? extends DbEnum>, Map<Object, DbEnum>> CACHE =
            new ConcurrentHashMap<>();

    private final Object value;
    private final String description;

    protected DbEnum(Object value, String description) {

        this.value = value;
        this.description = description;
        register();
    }

    /**
     * 自动注册到缓存
     */
    private void register() {
        Class<? extends DbEnum> enumClass = this.getClass();
        Map<Object, DbEnum> valueMap = CACHE.computeIfAbsent(
                enumClass,
                k -> new ConcurrentHashMap<>()
        );

        // 防止重复值
        if (valueMap.containsKey(value)) {
            throw new IllegalStateException("重复枚举值: " + value +
                    " in " + enumClass.getName());
        }

        valueMap.put(value, this);
    }

    // ============= 基础方法 =============
    public final Object getValue() {
        return value;
    }

    public final String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    // ============= 枚举风格静态方法 =============

    /**
     * 获取所有枚举值（返回不可修改集合）
     */
    public static <E extends DbEnum> List<E> values(Class<E> enumClass) {
        Map<Object, DbEnum> valueMap = getValueMap(enumClass);
        @SuppressWarnings("unchecked")
        Collection<E> values = (Collection<E>) valueMap.values();
        return Collections.unmodifiableList(new ArrayList<>(values));
    }

    /**
     * 获取所有枚举值（返回数组）
     */
    public static <E extends DbEnum> E[] valuesArray(Class<E> enumClass) {
        List<E> list = values(enumClass);
        @SuppressWarnings("unchecked")
        E[] array = (E[]) Array.newInstance(enumClass, list.size());
        return list.toArray(array);
    }

    /**
     * 获取枚举值映射
     */
    private static <E extends DbEnum> Map<Object, DbEnum> getValueMap(Class<E> enumClass) {
        Map<Object, DbEnum> valueMap = CACHE.get(enumClass);
        if (valueMap == null) {
            throw new IllegalStateException("枚举类未初始化: " + enumClass.getName());
        }
        return valueMap;
    }

    /**
     * 模拟枚举的valueOf()方法
     * 优化：单次遍历同时检查名称和值
     */
    public static <E extends DbEnum> E valueOf(Class<E> enumClass, Object identifier) {
        // 1. 优先使用高效的值查找
        E result = findByValue(enumClass, identifier);
        if (result != null) return result;

        // 2. 再尝试名称查找
        for (E value : values(enumClass)) {
            if (value.name().equals(identifier)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant " +
                enumClass.getName() + " for identifier: " + identifier);
    }

    // ============= 数据库值解析方法 =============

    /**
     * 根据值查找枚举（高效实现）
     */
    private static <E extends DbEnum> E findByValue(Class<E> enumClass, Object value) {
        Map<Object, DbEnum> valueMap = CACHE.get(enumClass);
        if (valueMap == null) return null;

        @SuppressWarnings("unchecked")
        E result = (E) valueMap.get(value);
        return result;
    }

    /**
     * 安全解析枚举
     */
    public static <E extends DbEnum> Optional<E> safeValueOf(Class<E> enumClass, Object value) {
        try {
            return Optional.of(valueOf(enumClass, value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 检查值是否有效
     */
    public static <E extends DbEnum> boolean isValid(Class<E> enumClass, Object value) {
        Map<Object, DbEnum> valueMap = CACHE.get(enumClass);
        return valueMap != null && valueMap.containsKey(value);
    }

    /**
     * 获取枚举名称 - 子类可覆盖
     * 优化：添加缓存提高性能
     */
    private transient String fieldName; // 缓存字段名

    public String name() {
        if (fieldName != null) {
            return fieldName;
        }

        // 默认实现：通过反射获取字段名
        Class<?> clazz = getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            try {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    Object fieldValue = field.get(null);
                    if (this == fieldValue) {
                        fieldName = field.getName(); // 缓存结果
                        return fieldName;
                    }
                }
            } catch (IllegalAccessException e) {
                // 忽略访问异常
            }
        }
        return toString();
    }
}
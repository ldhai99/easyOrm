package io.github.ldhai99.easyOrm.dao.core;


import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.annotation.Embeddable;
import io.github.ldhai99.easyOrm.annotation.TableField;
import io.github.ldhai99.easyOrm.dao.LambdaResolver;
import io.github.ldhai99.easyOrm.dao.orm.ClassFieldExplorer;
import io.github.ldhai99.easyOrm.dbenum.DbEnum;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;


public class FieldResolver {
    /**
     * @param <T> 泛型类型，表示实体类的类型
     * 获取属性名（字段名）
     * 该方法通过反射机制和函数式接口获取属性名，并将其转换为蛇形命名法
     * 用于ORM框架中生成SQL语句时的字段名映射
     *
     * @param getter 属性获取器接口的实例，用于获取实体类属性的值
     * @return 转换为蛇形命名法的属性名，用作SQL中的字段名
     */
    // ------------------------ 核心方法 ------------------------

    /**
     * 通过 Lambda 表达式解析字段名（优先处理 @TableField 注解）
     */

    public static <T> String field(PropertyGetter<T> getter) {
        try {
            SerializedLambda lambda = LambdaResolver.extractSerializedLambda(getter);
            String methodName = lambda.getImplMethodName();
            String propertyName = LambdaResolver.methodToProperty(methodName);
            // 通过反射获取字段上的 @TableField 注解
            Class<?> entityClass = LambdaResolver.getEntityClass(lambda);
            Field field = ClassFieldExplorer.findField(entityClass, propertyName);
            return resolveColumnName(field);
        } catch (Exception e) {
            // 注解解析失败时回退到原逻辑
            throw new RuntimeException("Failed to resolve field via Lambda", e);
        }
    }
    /**
     * 获取完整的列引用（表名.字段名）
     * /**
     * 该方法通过反射和lambda表达式提取实体类的属性信息，进而构建完整的SQL列引用
     * 这种方法避免了硬编码，提高了代码的可维护性和灵活性
     * @param <T>    泛型类型，表示某个类的类型
     * @param getter 一个属性获取器接口的实例，用于获取实体类属性的值
     * @return 返回格式为"表名.字段名"的字符串，表示数据库中的列引用
     */

    public static <T> String fullField(PropertyGetter<T> getter) {

        SerializedLambda lambda = LambdaResolver.extractSerializedLambda(getter);
        String methodName = lambda.getImplMethodName();
        String propertyName = LambdaResolver.methodToProperty(methodName);

        Class<?> entityClass = LambdaResolver.getEntityClass(lambda);
        Field field = ClassFieldExplorer.findField(entityClass, propertyName);
        String tableName = TableNameResolver.getTableName(entityClass);
        return SqlTools.camelToSnakeCase(tableName) + "." + resolveColumnName(field);

    }

    /**
     * 判断字段是否应在持久化/反射处理中被忽略。
     * <p>
     * 满足以下任一条件即返回 {@code true}（应忽略）：
     * <ul>
     *     <li>字段是 {@code static} 或 {@code transient}</li>
     *     <li>字段类型是 {@link Collection} 或 {@link Map}</li>
     *     <li>字段被 {@link TableField} 标记为 {@code exist = false}</li>
     *     <li>字段类型不是基础类型（见 {@link FieldResolver#isBasicType}），且：
     *         <ul>
     *             <li>不是 JPA {@code @Embeddable} 类</li>
     *             <li>不是数据库枚举类型（如 {@code DbEnum} 及其子类）</li>
     *         </ul>
     *     </li>
     * </ul>
     * </p>
     *
     * @param field 字段对象，不允许为 null
     * @return 是否应被忽略
     */
    public static boolean shouldIgnoreField(Field field) {
        Objects.requireNonNull(field, "field 不能为空");

        int modifiers = field.getModifiers();

        // 1. static 或 transient 字段
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return true;
        }

        Class<?> fieldType = field.getType();

        // 2. 集合或映射类型（不直接映射到数据库列）
        if (Collection.class.isAssignableFrom(fieldType) ||
                Map.class.isAssignableFrom(fieldType)) {
            return true;
        }

        // 3. 被 @TableField(exist = false) 标记
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && !tableField.exist()) {
            return true;
        }

        // 4. 基础类型（String、Number、Date、Enum 等）不忽略
        if (FieldResolver.isBasicType(fieldType)) {
            return false;
        }

        // 5. JPA @Embeddable 类（嵌入式对象），不忽略（可序列化为 JSON 等）
        if (fieldType.isAnnotationPresent(Embeddable.class)) {
            return false;
        }

        // 6. 数据库枚举类型（假设 DbEnum 是所有枚举的基类）
        if (DbEnum.class.isAssignableFrom(fieldType)) {
            return false;
        }

        // 默认：复杂自定义对象，应忽略
        return true;
    }
    /**
     * 判断类是否为“基础可序列化/可持久化类型”
     * <p>
     * 包括：
     * <ul>
     *     <li>基本类型及其包装类</li>
     *     <li>String、Date、Time、BigDecimal、UUID、byte[]</li>
     *     <li>枚举类型</li>
     * </ul>
     * </p>
     *
     * @param type 类型
     * @return 是否为基础类型
     */
    public static boolean isBasicType(Class<?> type) {
        if (type == null) {
            return false;
        }

        // 1. 基本类型及其包装类
        if (type.isPrimitive() ||
                Number.class.isAssignableFrom(type) ||
                type == Boolean.class ||
                type == Character.class) {
            return true;
        }

        // 2. 常见 JDK 时间与字符串类型
        return type == String.class ||
                type == Date.class ||
                type == java.sql.Date.class ||
                type == java.sql.Timestamp.class ||
                type == java.time.LocalDate.class ||
                type == java.time.LocalDateTime.class ||
                type == java.time.LocalTime.class ||
                type == java.time.Instant.class ||
                type == java.time.ZonedDateTime.class ||
                type == BigDecimal.class ||
                type == byte[].class ||
                type == UUID.class ||
                type.isEnum();
    }
    // ------------------------ 注解与字段解析 ------------------------

    /**
     * 解析字段的数据库列名（优先读取 @TableField 注解）
     */
    public static String resolveColumnName(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null && !tableField.value().isEmpty()) {
            return tableField.value(); // 注解优先
        } else {
            return SqlTools.camelToSnakeCase(field.getName()); // 默认驼峰转下划线
        }
    }


    /**
     * 将数据库列名解析为 Java 属性名（支持逆向查找 @TableField 注解）
     */
    public static String columnToProperty(String columnName, Class<?> clazz) {
        // 遍历类的所有字段，查找匹配的列名
        for (Field field : clazz.getDeclaredFields()) {
            String fieldColumnName = resolveColumnName(field); // 使用原有注解解析逻辑
            if (fieldColumnName.equalsIgnoreCase(columnName)) {
                return field.getName(); // 返回对应的属性名
            }
        }
        // 若未找到，尝试默认下划线转驼峰
        return SqlTools.snakeToCamelCase(columnName);
    }








}
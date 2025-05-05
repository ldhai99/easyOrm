package io.github.ldhai99.easyOrm.dao.core;


import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.annotation.TableField;
import io.github.ldhai99.easyOrm.dao.LambdaResolver;
import io.github.ldhai99.easyOrm.dao.ReflectionUtils;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;


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
            Field field = ReflectionUtils.findField(entityClass, propertyName);
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
        Field field = ReflectionUtils.findField(entityClass, propertyName);
        String tableName = TableNameResolver.getTableName(entityClass);
        return SqlTools.camelToSnakeCase(tableName) + "." + resolveColumnName(field);

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
package io.github.ldhai99.easyOrm.Lambda;



import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;


public class Field {
    /**
     * 获取属性名（字段名）
     */

    public static <T> String field(PropertyGetter<T> getter) {

        SerializedLambda lambda = extractSerializedLambda(getter);
        String methodName = lambda.getImplMethodName();
        String propertyName = extractPropertyName(methodName);
        return SqlTools.camelToSnakeCase(propertyName);
    }

    /**
     * 获取完整的列引用（表名.字段名）
     */
    public static <T> String fullField(PropertyGetter<T> getter) {

        SerializedLambda lambda = extractSerializedLambda(getter);

        String methodName = lambda.getImplMethodName();
        String propertyName = extractPropertyName(methodName);

        Class<?> entityClass = getEntityClass(lambda);
        String tableName = TableNameResolver.getTableName(entityClass);
        return SqlTools.camelToSnakeCase(tableName) + "." + SqlTools.camelToSnakeCase(propertyName);

    }

    private static <T> SerializedLambda extractSerializedLambda(PropertyGetter<T> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("解析失败，请检查 Lambda 是否可序列化", e);
        }
    }

    public static <T> Class<?> getEntityClass(SerializedLambda lambda) {
        try {

            String className = lambda.getImplClass().replace('/', '.');
            return Class.forName(className);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("解析失败，请检查 Lambda 是否可序列化", e);
        }
    }


    private static String extractPropertyName(String methodName) {
        if (methodName.startsWith("get")) {
            String raw = methodName.substring(3);
            return Character.toLowerCase(raw.charAt(0)) + raw.substring(1);
        } else if (methodName.startsWith("is")) {
            String raw = methodName.substring(2);
            return Character.toLowerCase(raw.charAt(0)) + raw.substring(1);
        }
        throw new IllegalArgumentException("非标准 Getter 方法: " + methodName);
    }


}
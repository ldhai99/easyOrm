package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class LambdaResolver {
    // ------------------------ Lambda 解析工具 ------------------------

    /**
     * 提取 Lambda 表达式的元信息
     */
    public static <T> SerializedLambda extractSerializedLambda(PropertyGetter<T> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("解析失败，请检查 Lambda 是否可序列化", e);
        }
    }

    /**
     * 从 Lambda 元信息中提取实体类
     */
    public static <T> Class<?> getEntityClass(SerializedLambda lambda) {
        try {

            String className = lambda.getImplClass().replace('/', '.');
            return Class.forName(className);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("解析失败，请检查 Lambda 是否可序列化", e);
        }
    }
    /**
     * 从方法名中提取属性名（支持 get/is 前缀）
     */
    public static String methodToProperty(String methodName) {
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

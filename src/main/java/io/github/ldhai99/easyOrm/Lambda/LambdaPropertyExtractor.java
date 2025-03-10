package io.github.ldhai99.easyOrm.Lambda;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;


public class LambdaPropertyExtractor {

    public static <T> String getPropertyName(SerializableGetter<T> getter) {
        try {
            Method method = getter.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(getter);
            String methodName = lambda.getImplMethodName();
            return extractPropertyName(methodName);
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
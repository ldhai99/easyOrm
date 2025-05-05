package io.github.ldhai99.easyOrm.dao.core;

import io.github.ldhai99.easyOrm.dao.DataTransformer;
import io.github.ldhai99.easyOrm.dao.MappingResolver;
import io.github.ldhai99.easyOrm.dao.ReflectionUtils;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanConverter {
    /**
     * 将 Map<String, Object> 转换为 Java Bean 实例
     *
     * @param data       数据源（键为数据库列名）
     * @param targetClass 目标 Java Bean 类型
     * @param <T>        泛型类型
     * @return 转换后的 Java Bean 实例
     */
    public static <T> T convertMapToBean(Map<String, Object> data, Class<T> targetClass) {
        try {
            T bean = targetClass.getDeclaredConstructor().newInstance();

            // ✅ 显式调用 MappingResolver 获取列名 -> 属性名的映射
            Map<String, String> columnToPropMap = MappingResolver.getColumnToPropMap(targetClass);

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String columnName = entry.getKey();
                String propertyName = columnToPropMap.get(columnName);
                if (propertyName == null) continue;

                Field field = ReflectionUtils.findField(targetClass, propertyName);
                if (field == null) continue;

                field.setAccessible(true);
                Object javaValue = DataTransformer.toJavaValue(field, entry.getValue());

                field.set(bean, javaValue);
            }

            return bean;
        } catch (Exception e) {
            throw new RuntimeException("转换 Map 到 Bean 失败", e);
        }
    }

    /**
     * 将 List<Map<String, Object>> 转换为 List<T>
     */
    public static <T> List<T> convertMapListToBeanList(List<Map<String, Object>> mapList, Class<T> targetClass) {
        List<T> result = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            result.add(convertMapToBean(map, targetClass));
        }
        return result;
    }
}
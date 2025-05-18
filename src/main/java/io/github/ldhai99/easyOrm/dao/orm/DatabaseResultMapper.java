package io.github.ldhai99.easyOrm.dao.orm;

import io.github.ldhai99.easyOrm.dao.except.BeanConvertException;
import io.github.ldhai99.easyOrm.dao.except.TypeConvertException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class DatabaseResultMapper {

    /**
     * 将 Map<String, Object> 转换为 Java Bean 实例
     *
     * @param data       数据源（键为数据库列名）
     * @param targetClass 目标 Java Bean 类型
     * @param <T>        泛型类型
     * @return 转换后的 Java Bean 实例
     */
    public static <T> T mapRowToBean(Map<String, Object> data, Class<T> targetClass) {
        try {
            T bean = targetClass.getDeclaredConstructor().newInstance();
            Map<String, String> columnToPropMap = MappingResolver.getColumnToPropMap(targetClass);

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String columnName = entry.getKey();
                String propertyName = columnToPropMap.get(columnName);

                if (propertyName == null) {
                    log.debug("未找到列 [{}] 的字段映射，目标类: {}", columnName, targetClass.getSimpleName());
                    continue;
                }


                Field field = ClassFieldExplorer.findField(targetClass, propertyName);
                if (field == null) {
                    log.warn("类 {} 中不存在字段: {}", targetClass.getSimpleName(), propertyName);

                    continue;
                }

                Object value = entry.getValue();
                if (value == null) continue;

                try {
                    Object javaValue = DataTransformer.toJavaValue(field.getType(), value);
                    field.setAccessible(true);
                    field.set(bean, javaValue);
                } catch (TypeConvertException e) {
                    String msg = String.format("字段 [%s] 类型转换失败: 数据库值类型=%s, 目标类型=%s, 值=%s",
                            field.getName(),
                            value.getClass().getSimpleName(),
                            field.getType().getSimpleName(),
                            value);
                    log.error(msg, e);
                    throw new BeanConvertException(msg, e);
                }
            }
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeanConvertException("创建Bean实例失败: " + targetClass.getName(), e);
        }
    }
    /**
     * 将 List<Map<String, Object>> 转换为 List<T>
     */
    public static <T> List<T> mapRowsToBeans(List<Map<String, Object>> mapList, Class<T> targetClass) {
        List<T> result = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            result.add(mapRowToBean(map, targetClass));
        }
        return result;
    }
}
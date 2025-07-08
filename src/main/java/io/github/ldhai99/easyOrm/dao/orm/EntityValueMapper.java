package io.github.ldhai99.easyOrm.dao.orm;
import io.github.ldhai99.easyOrm.annotation.Embeddable;
import io.github.ldhai99.easyOrm.dbenum.DbEnum;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EntityValueMapper {
    // 最大递归深度
    private static final int MAX_DEPTH = 5;

    public static <T> Map<String, Object> toColumnValues(T entity) {
        return toColumnValues(entity, false);
    }

    public static <T> Map<String, Object> toColumnValues(T entity, boolean ignoreNull) {
        try {
            Map<String, Object> columnValueMap = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();
            Map<String, String> propToColumnMap = EntityMetaMapper.propertyToColumn(clazz);

            // 处理所有字段
            processFields(entity, "", propToColumnMap, columnValueMap, ignoreNull, 0);

            return columnValueMap;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve entity fields", e);
        }
    }

    private static void processFields(Object entity, String prefix,
                                      Map<String, String> propToColumnMap,
                                      Map<String, Object> resultMap,
                                      boolean ignoreNull, int depth)
            throws IllegalAccessException {

        if (depth > MAX_DEPTH) {
            throw new IllegalStateException("Field nesting depth exceeded maximum limit: " + MAX_DEPTH);
        }

        if (entity == null) return;

        Class<?> clazz = entity.getClass();

        for (Map.Entry<String, String> entry : propToColumnMap.entrySet()) {
            String propertyPath = entry.getKey();
            String columnName = entry.getValue();

            // 如果当前字段不是以当前前缀开头，跳过
            if (!propertyPath.startsWith(prefix) || propertyPath.equals(prefix)) {
                continue;
            }

            // 提取当前层级的字段名
            String currentFieldName = propertyPath.substring(prefix.isEmpty() ? 0 : prefix.length() + 1);
            if (currentFieldName.contains(".")) {
                currentFieldName = currentFieldName.substring(0, currentFieldName.indexOf('.'));
            }

            Field field = ClassFieldExplorer.findField(clazz, currentFieldName);
            if (field == null) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(entity);

            // 处理空值
            if (value == null) {
                if (!ignoreNull) {
                    resultMap.put(columnName, null);
                }
                continue;
            }

            // 获取字段类型
            Class<?> fieldType = field.getType();
            // === 简化枚举处理：直接存储名称 ===
            if (fieldType.isEnum()) {
                Object dbValue = processEnumValue((Enum) value);
                resultMap.put(columnName, dbValue);
            }
            else  if (value instanceof DbEnum){
                Object dbValue = processDbEnumValue((DbEnum) value);
                resultMap.put(columnName, dbValue);
            }
            // 如果是可嵌入类型，递归处理
            else if (fieldType.isAnnotationPresent(Embeddable.class)) {
                // 构建新的前缀
                String newPrefix = prefix.isEmpty() ? currentFieldName : prefix + "." + currentFieldName;

                // 递归处理嵌套对象
                processFields(value, newPrefix, propToColumnMap, resultMap, ignoreNull, depth + 1);
            }
            // 如果是基本类型或值对象，直接添加到结果
            else if (SqlTools.isBasicType(fieldType)) {
                resultMap.put(columnName, value);
            }
            // 其他类型（如集合、关联实体）不做处理
        }
    }

    private static Object processDbEnumValue(DbEnum enumValue) {
        // 1 检查是否有 getValue() 方法（兼容模式）
        try {
            // 尝试调用 getValue() 方法
            return enumValue.getValue();
        } catch (Exception e) {
            // 如果失败，返回枚举名称
            return enumValue.name();
        }
    }
    private static Object processEnumValue(Enum enumValue) {
        // 1 检查是否有 getValue() 方法（兼容模式）
        try {
            // 尝试调用 getValue() 方法
            return enumValue.getClass().getMethod("getValue").invoke(enumValue);
        } catch (Exception e) {
            // 如果失败，返回枚举名称
            return enumValue.name();
        }
    }
}
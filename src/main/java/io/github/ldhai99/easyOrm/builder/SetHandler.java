package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.base.DataType;
import io.github.ldhai99.easyOrm.context.validation.Checker;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

public abstract class SetHandler <T extends SetHandler<T>> extends ColumnHandler<T> {

    //-------------------------------更新设置--------------------------------------------
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    //直接遍历字段，然后设置
    public T setMap(String fields, Map value) {

        //表示不通过fields来过滤字段，直接用Map中的key，vaule的值都是有用的
        if (fields == null || fields.equals("") || fields.equals("*")) {
             setMap(value);
            return self();
        }
        //通过fields来过滤Map的key,value中包含没用的key
        else {
            String[] parts = fields.split(",\\s*");
            for (String field : parts) {
                //通过值过滤，存在值就更新，不存在不更新，双过滤
                if (value.containsKey(field))
                    set(field, value.get(field));
            }
            return self();
        }

    }

    //直接用Map中的key，vaule的值都是有用的
    public T setMap(Map<String, Object> columnMap) {
        for (String key : columnMap.keySet()) {
            set(key,  columnMap.get(key));
        }
        return self();
    }

    //设置字段---值---------------------------------------------
    public T setNull(String name) {
        return set(name, null);
    }

    public <E> T setNull(PropertyGetter<E> getter, Object value) {
        return setNull(FieldResolver.fullField(getter));
    }
    /**
     * 无条件设置值（包括空值）
     */
    public T set(String name, Object value) {
        return setValue(name, value, null, true);
    }

    public <E> T set(PropertyGetter<E> getter, Object value) {
        return set(FieldResolver.fullField(getter), value);
    }


    /**
     * 不为 null 时设置值
     */
    public T setIfNotNull(String name, Object value) {
        if (value != null) {
            return setValue(name, value, null, true);
        }
        return self();
    }

    public <E> T setIfNotNull(PropertyGetter<E> getter, Object value) {
        return setIfNotNull(FieldResolver.fullField(getter), value);
    }

    /**
     * 不为空时设置值（广义空检查：包括空字符串、空集合等）
     */
    public T setIfNotEmpty(String name, Object value) {
        if (!Checker.isEmpty(value)) {
            return setValue(name, value, null, true);
        }
        return self();
    }

    public <E> T setIfNotEmpty(PropertyGetter<E> getter, Object value) {
        return setIfNotEmpty(FieldResolver.fullField(getter), value);
    }
    /**
     * 设置值，为空时使用数据类型默认值
     */
    public T setWithDefault(String name, Object value, DataType dataType) {
        if (Checker.isEmpty(value)) {
            value = getDefaultValue(dataType);
        }
        return setValue(name, value, dataType, true);
    }

    public <E> T setWithDefault(PropertyGetter<E> getter, Object value, DataType dataType) {
        return setWithDefault(FieldResolver.fullField(getter), value, dataType);
    }
    /**
     * 获取数据类型默认值
     */
    private Object getDefaultValue(DataType dataType) {
        if (dataType == null) return "";
        switch (dataType) {
            case NUMBER: return 0.0;
            case DATE: return new Date();
            default: return "";
        }
    }
    /**
     * 设置值，为空时使用自定义默认值
     */
    public T setWithDefault(String name, Object value, Object defaultValue) {
        if (Checker.isEmpty(value)) {
            value = defaultValue;
        }
        return setValue(name, value, null, true);
    }

    public <E> T setWithDefault(PropertyGetter<E> getter, Object value, Object defaultValue) {
        return setWithDefault(FieldResolver.fullField(getter), value, defaultValue);
    }

    //-------------------------------条件设置方法（条件参数在后）----------------------------------------
    /**
     * 条件设置值
     *
     * @param condition 条件为 true 时设置值
     */
    public T setIf(String name, Object value, boolean condition) {
        if (condition) {
            return set(name, value); // 调用公共的set方法
        }
        return self();
    }

    public <E> T setIf(PropertyGetter<E> getter, Object value, boolean condition) {
        return setIf(FieldResolver.fullField(getter), value, condition);
    }

    /**
     * 条件设置 NULL
     *
     * @param condition 条件为 true 时设置 NULL
     */
    public T setNullIf(String name, boolean condition) {
        if (condition) {
            return setNull(name); // 调用公共的setNull方法
        }
        return self();
    }

    public <E> T setNullIf(PropertyGetter<E> getter, boolean condition) {
        return setNullIf(FieldResolver.fullField(getter), condition);
    }

    /**
     * 带条件的设置（使用谓词函数）
     *
     * @param predicate 值判断函数，返回 true 时设置值
     */
    public T setIf(String name, Object value, Predicate<Object> predicate) {
        if (predicate.test(value)) {
            return set(name, value); // 调用公共的set方法
        }
        return self();
    }

    public <E> T setIf(PropertyGetter<E> getter, Object value, Predicate<Object> predicate) {
        return setIf(FieldResolver.fullField(getter), value, predicate);
    }

    /**
     * 带条件的默认值设置
     *
     * @param condition 条件为 true 时设置值，false 时设置默认值
     */
    public T setWithDefaultIf(String name, Object value, Object defaultValue, boolean condition) {
        if (condition) {
            return set(name, value);
        } else {
            return set(name, defaultValue);
        }
    }

    public <E> T setWithDefaultIf(PropertyGetter<E> getter, Object value, Object defaultValue, boolean condition) {
        return setWithDefaultIf(FieldResolver.fullField(getter), value, defaultValue, condition);
    }
//-------------------------------内部实现-------------------------------------------

    /**
     * 核心值处理方法
     */
//    protected T setValue(String name, Object value) {
//        //设置占位符号
//        setPlaceholder(name, jdbcModel.processSqlValue(value));
//
//        return self();
//    }
    private T setValue(String name, Object value, DataType dataType, boolean allowNull) {
        Object processedValue = processValue(value, dataType, allowNull);
        setPlaceholder(name, jdbcModel.processSqlValue(processedValue));
        return self();
    }
    /**
     * 值处理逻辑
     */
    private Object processValue(Object value, DataType dataType, boolean allowNull) {
        // 空值处理
        if (value == null) {
            if (allowNull) return null;
            return getDefaultValue(dataType);
        }

        // 类型转换
        if (dataType != null) {
            try {
                switch (dataType) {
                    case NUMBER:
                        if (value instanceof Number) return value;
                        return Double.parseDouble(value.toString().trim());
                    case DATE:
                        if (value instanceof Date) return value;
                        return parseDate(value.toString());
                    default:
                        return value.toString().trim();
                }
            } catch (Exception e) {
                // 转换失败时返回默认值
                return getDefaultValue(dataType);
            }
        }

        // 无指定类型时返回原始值
        return value;
    }
    private Date parseDate(Object value) throws ParseException {
        if (value instanceof Date) {
            return (Date) value;
        }
        return DATE_FORMAT.parse(value.toString().trim());
    }
    private void setPlaceholder(String name, String nameParamPlaceholder) {

        //更新时候，存set
        if (builder.isUpdate()) {
            this.builder.set(" " + name + " = " + nameParamPlaceholder);
        }
        //增加时候，set存列与值
        else if (builder.isInsert()) {
            this.builder.insertColumn(name, nameParamPlaceholder);
        }

    }



}

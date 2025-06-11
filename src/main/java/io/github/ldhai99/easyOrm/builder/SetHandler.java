package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.DbParameter;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;

import java.util.Map;

public class SetHandler <T extends SetHandler<T>> extends ColumnHandler<T> {

    //-------------------------------更新设置--------------------------------------------


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
            for (int i = 0; i < parts.length; i++) {
                //通过值过滤，存在值就更新，不存在不更新，双过滤
                if (value.containsKey(parts[i]))
                    set(parts[i], new DbParameter(parts[i], value.get(parts[i])));
            }
            return self();
        }

    }

    //直接用Map中的key，vaule的值都是有用的
    public T setMap(Map<String, Object> columnMap) {
        for (String key : columnMap.keySet()) {
            set(key, new DbParameter(key, columnMap.get(key), null));

        }
        return self();
    }

    //设置字段---值---------------------------------------------
    public T set(String name, Object value) {

        set(name, new DbParameter(name, value));
        return self();
    }

    public <E> T set(PropertyGetter<E> getter, Object value) {
        return set(FieldResolver.fullField(getter), value);
    }

    //不为空时候更新
    public T setIfNotNull(String name, Object value) {
        if (value == null)
            return self();

        set(name, new DbParameter(name, value));
        return self();
    }

    public <E> T setIfNotNull(PropertyGetter<E> getter, Object value) {
        return setIfNotNull(FieldResolver.fullField(getter), value);
    }

    public T set(String name, Object value, String datatype) {
        set(name, new DbParameter(name, value, datatype));
        return self();
    }

    public <E> T set(PropertyGetter<E> getter, Object value, String datatype) {
        return set(FieldResolver.fullField(getter), value, datatype);
    }

    public T set(String name, Object value, String datatype, boolean allowNull) {
        set(name, new DbParameter(name, value, datatype, allowNull));
        return self();
    }

    public <E> T set(PropertyGetter<E> getter, Object value, String datatype, boolean allowNull) {
        return set(FieldResolver.fullField(getter), value, datatype, allowNull);
    }

    public T set(String name, DbParameter pmt) {
        set1(name, pmt.getValue());
        return self();

    }

    public <E> T set(PropertyGetter<E> getter, DbParameter pmt) {
        return set(FieldResolver.fullField(getter), pmt);
    }

    protected T set1(String name, Object subSql) {
        //设置占位符号
        setPlaceholder(name, jdbcModel.processSqlValue(subSql));

        return self();
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

    //参数设置--------------------------------------------

    public T setValue(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlValue(subSql));
        return self();
    }

    public <E> T setValue(PropertyGetter<E> getter, Object subSql) {
        return setValue(FieldResolver.fullField(getter), subSql);
    }

    public T setValue$(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlName(subSql));
        return self();
    }

    public <E> T setValue$(PropertyGetter<E> getter, Object subSql) {
        return setValue$(FieldResolver.fullField(getter), subSql);
    }


}

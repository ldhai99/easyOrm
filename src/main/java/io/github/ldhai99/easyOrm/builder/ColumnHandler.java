package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;

public class ColumnHandler<T extends ColumnHandler<T>> extends BaseSQL<T> {
    //代理SqlDataModel---------------------------
//------------------------------聚合函数列----------------------------------------
    //---sum--------------
    public T sum(String column) {
        return aggregate("sum", column);
    }

    public <E> T sum(PropertyGetter<E> getter) {
        return sum(getter, false);
    }

    public <E> T sumfull(PropertyGetter<E> getter) {
        return sum(getter, true);
    }

    public <E> T sum(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return sum(FieldResolver.fullField(getter));
        else
            return sum(FieldResolver.field(getter));
    }

    //---sum-----alias---------
    public T sum(String column, String alias) {
        return aggregate("sum", column, alias);
    }

    public <E> T sum(PropertyGetter<E> getter, String alias) {
        return sum(getter, alias, false);
    }

    public <E> T sumfull(PropertyGetter<E> getter, String alias) {
        return sum(getter, alias, true);
    }

    public <E> T sum(PropertyGetter<E> getter, String alias, boolean usefull) {
        if (usefull)
            return sum(FieldResolver.fullField(getter), alias);
        else
            return sum(FieldResolver.field(getter), alias);
    }

    //---avg--------------
    public T avg(String column) {
        return aggregate("avg", column);
    }


    public <E> T avg(PropertyGetter<E> getter) {
        return avg(getter, false);
    }

    public <E> T avgfull(PropertyGetter<E> getter) {
        return avg(getter, true);
    }

    public <E> T avg(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return avg(FieldResolver.fullField(getter));
        else
            return avg(FieldResolver.field(getter));
    }

    //---avg-----alias---------
    public T avg(String column, String alias) {
        return aggregate("avg", column, alias);
    }

    public <E> T avg(PropertyGetter<E> getter, String alias) {
        return avg(getter, alias, false);
    }

    public <E> T avgfull(PropertyGetter<E> getter, String alias) {
        return sum(getter, alias, true);
    }

    public <E> T avg(PropertyGetter<E> getter, String alias, boolean usefull) {
        if (usefull)
            return sum(FieldResolver.fullField(getter), alias);
        else
            return sum(FieldResolver.field(getter), alias);
    }

    //---max--------------
    public T max(String column) {
        return aggregate("max", column);
    }


    public <E> T max(PropertyGetter<E> getter) {
        return max(getter, false);
    }

    public <E> T maxfull(PropertyGetter<E> getter) {
        return max(getter, true);
    }

    public <E> T max(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return max(FieldResolver.fullField(getter));
        else
            return max(FieldResolver.field(getter));
    }

    //---max--alis------------
    public T max(String column, String alias) {
        return aggregate("max", column, alias);
    }


    public <E> T max(PropertyGetter<E> getter, String alias) {
        return max(getter, alias, false);
    }

    public <E> T maxfull(PropertyGetter<E> getter, String alias) {
        return max(getter, alias, true);
    }

    public <E> T max(PropertyGetter<E> getter, String alias, boolean usefull) {
        if (usefull)
            return max(FieldResolver.fullField(getter), alias);
        else
            return max(FieldResolver.field(getter), alias);
    }

    //---min--------------
    public T min(String column) {
        return aggregate("min", column);
    }


    public <E> T min(PropertyGetter<E> getter) {
        return min(getter, false);
    }

    public <E> T minfull(PropertyGetter<E> getter) {
        return min(getter, true);
    }

    public <E> T min(PropertyGetter<E> getter, boolean usefull) {
        if (usefull)
            return min(FieldResolver.fullField(getter));
        else
            return min(FieldResolver.field(getter));
    }

    //---min--alis------------
    public T min(String column, String alias) {
        return aggregate("min", column, alias);
    }


    public <E> T min(PropertyGetter<E> getter, String alias) {
        return min(getter, alias, false);
    }

    public <E> T minfull(PropertyGetter<E> getter, String alias) {
        return min(getter, alias, true);
    }

    public <E> T min(PropertyGetter<E> getter, String alias, boolean usefull) {
        if (usefull)
            return min(FieldResolver.fullField(getter), alias);
        else
            return min(FieldResolver.field(getter), alias);
    }

    //---count--------------
    public T count() {
        this.builder.column("count(" + "*" + ") ");
        return self();
    }

    //---count--alis------------
    public T count(String alias) {
        this.builder.column("count(" + "*" + ") " + alias + " ");
        return self();
    }

    //---aggregate--------------
    public T aggregate(String function, String column) {
        return aggregate(function, column, null);
    }

    public <E> T aggregate(String function, PropertyGetter<E> getter) {
        return aggregate(function, getter, null, false);
    }

    public <E> T aggregatefull(String function, PropertyGetter<E> getter) {
        return aggregate(function, getter, null, true);
    }

    public <E> T aggregate(String function, PropertyGetter<E> getter, boolean usefull) {
        return aggregate(function, getter, null, usefull);
    }

    //---aggregate--alis------------
    public T aggregate(String function, String column, String alias) {
        String alias1 = alias;
        if (alias1 == null)
            alias1 = column;
        this.builder.column(" " + function + "(" + column + ") " + alias1 + " ");

        return self();
    }

    public <E> T aggregate(String function, PropertyGetter<E> getter, String alias) {
        return aggregate(function, getter, alias, false);
    }

    public <E> T aggregatefull(String function, PropertyGetter<E> getter, String alias) {
        return aggregate(function, getter, alias, true);
    }

    public <E> T aggregate(String function, PropertyGetter<E> getter, String alias, boolean usefull) {
        if (usefull)
            return aggregate(function, FieldResolver.fullField(getter), alias);
        else
            return aggregate(function, FieldResolver.field(getter), alias);
    }

    //-------------------------查询列----------------------------------------------------
    public T column(String column) {
        this.builder.column(column);
        return self();
    }

    public <E> T column(PropertyGetter<E> getter) {
        return column(FieldResolver.field(getter));
    }


    public <E> T full(PropertyGetter<E> getter) {
        return column(FieldResolver.fullField(getter));
    }

    public <E> T column(PropertyGetter<E>... getters) {
        for (int i = 0; i < getters.length; i++) {
            column(FieldResolver.field(getters[i]));
        }
        return self();
    }

    public <E> T full(PropertyGetter<E>... getters) {
        for (int i = 0; i < getters.length; i++) {
            column(FieldResolver.fullField(getters[i]));
        }
        return self();
    }

    //---列别名-----------------------
    public T column(String column, String alias) {
        this.builder.column(column, alias);
        return self();
    }

    public <E> T column(PropertyGetter<E> getter, String alias) {
        return column(FieldResolver.field(getter), alias);
    }

    public <E> T full(PropertyGetter<E> getter, String alias) {
        return column(FieldResolver.fullField(getter), alias);
    }

    //---表.字段 列别名-----------------------
    public T column(String tableAlias, String column, String alias) {
        this.builder.column(tableAlias + "." + column, alias);
        return self();
    }

    public T column(Class<?> clazz, String column, String alias) {
        this.builder.column(TableNameResolver.getTableName(clazz) + "." + column, alias);
        return self();
    }

    public <E> T column(String tableAlias, PropertyGetter<E> getter, String alias) {
        return column(tableAlias, FieldResolver.field(getter), alias);
    }

    public <E> T column(Class<?> clazz, PropertyGetter<E> getter, String alias) {
        return column(TableNameResolver.getTableName(clazz), FieldResolver.field(getter), alias);
    }

    //---列加分组-----------------------
    public T column(String column, boolean groupBy) {
        this.builder.column(column, groupBy);
        return self();
    }

    public <E> T column(PropertyGetter<E> getter, boolean groupBy) {
        return column(FieldResolver.field(getter), groupBy);
    }

    public <E> T full(PropertyGetter<E> getter, boolean groupBy) {
        return column(FieldResolver.fullField(getter), groupBy);
    }

    //子查询列----------------------------------------------------
    public T column(T subSql, String alias) {
        this.builder.column(jdbcModel.processSqlName(subSql) + alias + " ");
        return self();
    }




}

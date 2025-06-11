package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;
import io.github.ldhai99.easyOrm.dao.orm.EntityMetaMapper;

import java.util.Map;

public class ColumnHandler<T extends ColumnHandler<T>> extends BaseSQL<T> {
    //代理SqlDataModel---------------------------
//------------------------------聚合函数列----------------------------------------
    //---sum--------------
    public T sum(String column) {
        return aggregate("sum", column);
    }

    public <E> T sum(PropertyGetter<E> getter) {
        return sum(FieldResolver.fullField(getter));
    }



    //---sum-----alias---------
    public T sum(String column, String alias) {
        return aggregate("sum", column, alias);
    }

    public <E> T sum(PropertyGetter<E> getter, String alias) {
        return sum(FieldResolver.fullField(getter), alias);
    }




    //---avg--------------
    public T avg(String column) {
        return aggregate("avg", column);
    }


    public <E> T avg(PropertyGetter<E> getter) {
        return avg(FieldResolver.fullField(getter));
    }



    //---avg-----alias---------
    public T avg(String column, String alias) {
        return aggregate("avg", column, alias);
    }

    public <E> T avg(PropertyGetter<E> getter, String alias) {

        return avg(FieldResolver.fullField(getter), alias);
    }




    //---max--------------
    public T max(String column) {
        return aggregate("max", column);
    }


    public <E> T max(PropertyGetter<E> getter) {
        return max(FieldResolver.fullField(getter));
    }


    //---max--alis------------
    public T max(String column, String alias) {
        return aggregate("max", column, alias);
    }


    public <E> T max(PropertyGetter<E> getter, String alias) {
        return max(FieldResolver.fullField(getter), alias);
    }




    //---min--------------
    public T min(String column) {
        return aggregate("min", column);
    }


    public <E> T min(PropertyGetter<E> getter) {
        return min(FieldResolver.fullField(getter));
    }




    //---min--alis------------
    public T min(String column, String alias) {
        return aggregate("min", column, alias);
    }


    public <E> T min(PropertyGetter<E> getter, String alias) {
        return min(FieldResolver.fullField(getter), alias);
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
        return aggregate(function, getter, null);
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
        return aggregate(function, FieldResolver.fullField(getter), alias);
    }




    //-------------------------查询列----------------------------------------------------
    public T column(String column) {
        this.builder.column(column);
        return self();
    }

    public <E> T column(PropertyGetter<E> getter) {
        return column(FieldResolver.fullField(getter));
    }



    public <E> T column(PropertyGetter<E>... getters) {
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
        return column(FieldResolver.fullField(getter), groupBy);
    }



    //子查询列----------------------------------------------------
    public T column(T subSql, String alias) {
        this.builder.column(jdbcModel.processSqlName(subSql) + alias + " ");
        return self();
    }
    public T column(T subSql) {
        this.builder.column(jdbcModel.processSqlName(subSql) + "" + " ");
        return self();
    }

    /**
     * 添加类对应表的所有字段（逐个添加）
     */
    public <E> T column(Class<E> entityClass) {
        return column(entityClass, TableNameResolver.getTableName(entityClass));
    }

    /**
     * 添加类对应表的所有字段 + 表别名
     */
    public <E> T column(Class<E> entityClass, String tableAlias) {
        Map<String, String> propToColumnMap = EntityMetaMapper.propertyToColumn(entityClass);
        for (String propertyName : propToColumnMap.keySet()) {
            String columnName = propToColumnMap.get(propertyName);
            this.builder.column(tableAlias + "." + columnName);
        }
        return self();
    }

    /**
     * 使用 .* 方式添加所有字段（适合快速查询）
     */
    public <E> T columnStar(Class<E> entityClass) {
        return columnStar(entityClass, TableNameResolver.getTableName(entityClass));
    }

    /**
     * 使用 .* 方式 + 别名
     */
    public <E> T columnStar(Class<E> entityClass, String tableAlias) {
        this.builder.column(tableAlias + ".*");
        return self();
    }

}

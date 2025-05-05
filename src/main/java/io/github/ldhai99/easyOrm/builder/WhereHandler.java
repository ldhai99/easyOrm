package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.util.*;

public class WhereHandler<T extends WhereHandler<T>> extends SetHandler<T> {
//0、通用查询-----------age=18----------------------------------------------------

    //----------多表连接------------------------------
    public T join(String join) {
        this.builder.join(join);
        return self();
    }
    //inner join---

    public T join(String table, String on) {
        this.join("inner join", table, on);
        return self();
    }

    public T join(Class clazz, String on) {
        return join(TableNameResolver.getTableName(clazz), on);
    }

    public T join(String table, T subSql) {
        return this.join( table, jdbcModel.processSqlName(subSql));

    }
    public T join(Class clazz, T subSql) {

        return self().join(TableNameResolver.getTableName(clazz),subSql);
    }
    //leftJoin---
    public T leftJoin(String table, String on) {
        this.join("left join", table, on);
        return self();
    }

    public T leftJoin(Class<?> clazz, String on) {
        return leftJoin(TableNameResolver.getTableName(clazz), on);
    }


    public T leftJoin(String table, T subSql) {
        return this.leftJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T leftJoin(Class clazz, T subSql) {

        return self().leftJoin(TableNameResolver.getTableName(clazz),subSql);
    }

    //rightJoin---
    public T rightJoin(String table, String on) {
        this.join("right join", table, on);
        return self();
    }

    public T rightJoin(Class<?> clazz, String on) {
        return rightJoin(TableNameResolver.getTableName(clazz), on);
    }


    public T rightJoin(String table, T subSql) {
        return this.rightJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T rightJoin(Class clazz, T subSql) {

        return self().rightJoin(TableNameResolver.getTableName(clazz),subSql);
    }
    //fullJoin---
    public T fullJoin(String table, String on) {
        this.join("full join", table, on);
        return self();
    }

    public T fullJoin(Class<?> clazz, String on) {
        return rightJoin(TableNameResolver.getTableName(clazz), on);
    }



    public T fullJoin(String table, T subSql) {
        return this.fullJoin( table, jdbcModel.processSqlName(subSql));

    }
    public T fullJoin(Class clazz, T subSql) {

        return self().fullJoin(TableNameResolver.getTableName(clazz),subSql);
    }
//------
    public T join(String join, String table, String on) {
        if (on != null && on.trim().length() != 0) {
            this.builder.join(join + " " + table + " on " + on);
        } else
            this.builder.join(join + " " + table);
        return self();
    }

    //一、比较谓词----------------------------------------------------
    public T func(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    //一、比较谓词----------------------------------------------------
    public T where(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    public <E> T where(PropertyGetter<E> getter, Object value) {
        return where(FieldResolver.field(getter), value);
    }

    public T eqMap(Map<String, Object> columnMap) {
        if(SqlTools.isEmpty(columnMap))
            return self();
        for (String key : columnMap.keySet()) {
            eq(key, columnMap.get(key));
        }
        return self();
    }

    public T eq(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    // 新增支持Lambda的eq方法
// 支持泛型的 eq 方法<E> String field(PropertyGetter<E> getter)
    public <E> T eq(PropertyGetter<E> getter, Object value) {
        return eq(FieldResolver.field(getter), value);
    }

    public T eqIfNotNull(Object name, Object value) {
        if(value==null)
            return self();
        return nameOperatorValue(name, "=", value);
    }

    public <E> T eqIfNotNull(PropertyGetter<E> getter, Object value) {
        return eqIfNotNull(FieldResolver.field(getter), value);
    }
    public T eqIfNotEmpty(Object name, Object value) {
        if(SqlTools.isEmpty(value))
            return self();
        return nameOperatorValue(name, "=", value);
    }

    public <E> T eqIfNotEmpty(PropertyGetter<E> getter, Object value) {
        return eqIfNotEmpty(FieldResolver.field(getter), value);
    }

    public T neq(Object name, Object value) {
        return nameOperatorValue(name, "<>", value);
    }

    public <E> T neq(PropertyGetter<E> getter, Object value) {
        return neq(FieldResolver.field(getter), value);
    }

    public T gt(Object name, Object value) {
        return nameOperatorValue(name, ">", value);
    }

    public <E> T gt(PropertyGetter<E> getter, Object value) {
        return gt(FieldResolver.field(getter), value);
    }

    public T gte(Object name, Object value) {
        return nameOperatorValue(name, ">=", value);
    }

    public <E> T gte(PropertyGetter<E> getter, Object value) {
        return gte(FieldResolver.field(getter), value);
    }

    public T lt(Object name, Object value) {
        return nameOperatorValue(name, "<", value);
    }

    public <E> T lt(PropertyGetter<E> getter, Object value) {
        return lt(FieldResolver.field(getter), value);
    }

    public T lte(Object name, Object value) {
        return nameOperatorValue(name, "<=", value);
    }

    public <E> T lte(PropertyGetter<E> getter, Object value) {
        return lte(FieldResolver.field(getter), value);
    }

    //name+operator+value
    protected T nameOperatorValue(Object name, String operator, Object value) {
        if(value==null)
            return self();

        this.where(String.format(" %s %s %s ", jdbcModel.processSqlName(name), operator, jdbcModel.processSqlValue(value)));
        return self();
    }

    //二、LIKE 谓词——字符串的部分一致查询
    public T like(Object name, Object value) {
        return likeOperator(name, "like", value);
    }

    public <E> T like(PropertyGetter<E> getter, Object value) {
        return like(FieldResolver.field(getter), value);
    }

    public T like_(Object name, Object value) {
        return likeOperator(name, "like_", value);
    }

    public <E> T like_(PropertyGetter<E> getter, Object value) {
        return like_(FieldResolver.field(getter), value);
    }

    public T notLike(Object name, Object value) {
        return likeOperator(name, "notLike", value);
    }

    public <E> T notLike(PropertyGetter<E> getter, Object value) {
        return notLike(FieldResolver.field(getter), value);
    }

    public T likeLeft(Object name, Object value) {
        return likeOperator(name, "likeLeft", value);
    }

    public <E> T likeLeft(PropertyGetter<E> getter, Object value) {
        return likeLeft(FieldResolver.field(getter), value);
    }

    public T likeRight(Object name, Object value) {
        return likeOperator(name, "likeRight", value);
    }

    public <E> T likeRight(PropertyGetter<E> getter, Object value) {
        return likeRight(FieldResolver.field(getter), value);
    }

    public T notLikeLeft(Object name, Object value) {
        return likeOperator(name, "notLikeLeft", value);
    }

    public <E> T notLikeLeft(PropertyGetter<E> getter, Object value) {
        return notLikeLeft(FieldResolver.field(getter), value);
    }

    public T notLikeRight(Object name, Object value) {
        return likeOperator(name, "notLikeRight", value);
    }

    public <E> T notLikeRight(PropertyGetter<E> getter, Object value) {
        return notLikeRight(FieldResolver.field(getter), value);
    }

    protected T likeOperator(Object name, String operator, Object value) {

        Object newvalue = value;

        //对值预处理
        if (!(value instanceof SQL)) {
            String oldValue = (String) value;

            if (operator.equalsIgnoreCase("like"))
                newvalue = "%" + oldValue + "%";
            else if (operator.equalsIgnoreCase("notlike")) {
                newvalue = " not %" + oldValue + "%";
            } else if (operator.equalsIgnoreCase("likeLeft")) {
                newvalue = oldValue + "%";
            } else if (operator.equalsIgnoreCase("likeRight")) {
                newvalue = "%" + oldValue;
            } else if (operator.equalsIgnoreCase("notlikeLeft")) {
                newvalue = " not " + oldValue + "%";
            } else if (operator.equalsIgnoreCase("notlikeRight")) {
                newvalue = " not %" + oldValue;
            } else if (operator.equalsIgnoreCase("like_")) {
                newvalue = oldValue;
            }
        }
        String namePlacehoder = jdbcModel.processSqlName(name);
        String valuePlacehoder = jdbcModel.processSqlValue(newvalue);

        if (operator.equalsIgnoreCase("notlike")) {
            this.where(namePlacehoder + " not like " + valuePlacehoder);
        } else {
            this.where(namePlacehoder + " like " + valuePlacehoder);
        }
        return self();
    }

    //三、BETWEEN 谓词——范围查询
    public T between(Object name, Object value1, Object value2) {
        this.between(name, "  between ", value1, value2);

        return self();
    }

    public <E> T between(PropertyGetter<E> getter, Object value1, Object value2) {
        return between(FieldResolver.field(getter), value1, value2);
    }

    public T notBetween(Object name, Object value1, Object value2) {

        this.between(name, "  not between ", value1, value2);

        return self();
    }

    public <E> T notBetween(PropertyGetter<E> getter, Object value1, Object value2) {
        return notBetween(FieldResolver.field(getter), value1, value2);
    }

    protected T between(Object name, String between, Object value1, Object value2) {
        String placehoder1 = jdbcModel.processSqlValue(value1);
        String placehoder2 = jdbcModel.processSqlValue(value2);
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + between + placehoder1 + " and " + placehoder2);

        return self();
    }

    //四、IS NULL、IS NOT NULL——判断是否为 NULL
    //-------4.1 IS NULL-------------------------------
    public T isNullElseEq(Object name, Object value) {
        if(value==null)
            return isNull(name);
        else
            return eq(name, value);
    }
    public <E> T isNullElseEq(PropertyGetter<E> getter, Object value) {
        return isNullElseEq(FieldResolver.field(getter),value);
    }
    public T isNull(Object name,Object value) {
        if(value==null)
            return this.isNull(name);
        else
            return self();
    }
    public <E> T isNull(PropertyGetter<E> getter,Object value) {
        return isNull(FieldResolver.field(getter),value);
    }
    public T isNull(Object name) {

        this.isNull(name, "is  null");
        return self();
    }

    public <E> T isNull(PropertyGetter<E> getter) {
        return isNull(FieldResolver.field(getter));
    }
    //-------4.2 IS not NULL----------------------------
    public T isNotNull(Object name,Object value) {
        if(value!=null)
            return this.isNotNull(name);
        else
            return self();
    }
    public <E> T isNotNull(PropertyGetter<E> getter,Object value) {
        return isNotNull(FieldResolver.field(getter),value);
    }
    public T isNotNull(Object name) {

        this.isNull(name, "is not null");
        return self();
    }

    public <E> T isNotNull(PropertyGetter<E> getter) {
        return isNotNull(FieldResolver.field(getter));
    }
    //-------4.0 IS  NULL
    protected T isNull(Object name, String isNull) {
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + " " + isNull);
        return self();
    }
    //-------4.3 IS  empty
    public T isEmptyElseEq(Object name, Object value) {
        if(SqlTools.isEmpty(value))
            return isEmpty(name);
        else
            return eq(name, value);
    }
    public <E> T isEmptyElseEq(PropertyGetter<E> getter, Object value) {
        return isEmptyElseEq(FieldResolver.field(getter),value);
    }
    public T isEmpty(Object name, Object value) {
        if(SqlTools.isEmpty(value))
            return isEmpty(name);
        else
            return self();
    }
    public <E> T isEmpty(PropertyGetter<E> getter, Object value) {
        return isEmpty(FieldResolver.field(getter),value);
    }
    public <E> T isEmpty(PropertyGetter<E> getter) {
        return isEmpty(FieldResolver.field(getter));
    }
    public T isEmpty(Object name) {
        this.begin().eq(name, "").or().isNull(name).end();
        return self();
    }

    //-------4.3 IS  not empty
    public T isNotEmpty(Object name, Object value   ) {
        if(SqlTools.isNotEmpty(value))
            return isNotEmpty(name);
        else
            return self();
    }
    public <E> T isNotEmpty(PropertyGetter<E> getter, Object value) {
        return isNotEmpty(FieldResolver.field(getter),  value);
    }

    //不为空，不为null，并且不为''
    public T isNotEmpty(Object name) {

        this.neq(name, "").isNotNull(name);
        return self();
    }

    public <E> T isNotEmpty(PropertyGetter<E> getter) {
        return isNotEmpty(FieldResolver.field(getter));
    }


    //五、IN 谓词——OR 的简便用法
    public T or() {

        this.where(" or ");
        return self();
    }
    public T and() {

        this.where(" and ");
        return self();
    }
    public T in(Object name, List<?> values) {
        return nameOperatorValue(name, "in", values);

    }

    public <E> T in(PropertyGetter<E> getter, List<?> values) {
        return in(FieldResolver.field(getter), values);
    }

    public T in(Object name, Object... values) {

        return nameOperatorValue(name, "in", new ArrayList<>(Arrays.asList(values)));
    }

    public <E> T in(PropertyGetter<E> getter, Object... values) {
        return in(FieldResolver.field(getter), values);
    }

    public T in(Object name, T subSql) {

        return nameOperatorValue(name, "in", subSql);
    }

    public <E> T in(PropertyGetter<E> getter, T subSql) {
        return in(FieldResolver.field(getter), subSql);
    }

    public T notIn(Object name, List<?> values) {
        return nameOperatorValue(name, "not in", values);
    }

    public <E> T notIn(PropertyGetter<E> getter, List<?> values) {
        return notIn(FieldResolver.field(getter), values);
    }

    public T notIn(Object name, T subSql) {

        return nameOperatorValue(name, "not in", subSql);
    }

    public <E> T notIn(PropertyGetter<E> getter, T subSql) {
        return notIn(FieldResolver.field(getter), subSql);
    }

    public T notIn(Object name, Object... values) {
        return nameOperatorValue(name, "not in", new ArrayList<>(Arrays.asList(values)));

    }

    public <E> T notIn(PropertyGetter<E> getter, Object... values) {
        return notIn(FieldResolver.field(getter), values);
    }

    //七、EXIST 谓词
    //
    public T exists(T subSQL) {
        return operatorValue("exists", subSQL);
    }

    public T notExists(T subSQL) {
        return operatorValue("not exists", subSQL);
    }

    //operator+value------exists（value）-------
    protected T operatorValue(String operator, T subSQL) {
        this.where(String.format("  %s %s ", operator, jdbcModel.processSqlValue(subSQL)));
        return self();
    }

    //八、括号
    public T begin() {

        this.where(" ( ");
        return self();
    }

    public T end() {

        this.where(" ) ");
        return self();
    }


}

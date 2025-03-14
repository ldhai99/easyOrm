package io.github.ldhai99.easyOrm;


import io.github.ldhai99.easyOrm.Lambda.LambdaExtractor;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.Lambda.TableNameResolver;
import io.github.ldhai99.easyOrm.executor.DbUtilsExecutor;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;


public class SQL {
    private static final long serialVersionUID = 1L;

    protected SqlModel builder = new SqlModel();
    protected JdbcModel jdbcModel = new JdbcModel();

    private Executor executor;

    public static void main(String[] args) {
        SQL read = new SQL().column("*")
                .column(new SQL().column("age").from("student")
                        .between("age", 1, 100), "age1")

                .from("student")
                .eq("age", 18).or()
                .begin().eq("age", 18).eq("age", 18).end()
                .begin()
                .in("age", new ArrayList<>(Arrays.asList(1, 2, 3))).end()
                .between("age", 1, 20).
                in("age", new SQL().column("age").from("student")
                        .between("age", 1, 100))
                .between("age", 2, 99)
                .exists(new SQL().column("age").from("student")
                        .between("age", 3, 98));

        System.out.println(read.toString());
        System.out.println(read.getParameterMap());

    }

    //构造方法
    public SQL() {
        createExecutor();
    }

    //构造函数，传入连接，传入数据源
    public SQL(Connection connection) {
        createExecutor(connection);
    }

    //构造函数，传入数据源
    public SQL(DataSource dataSource) {
        createExecutor(dataSource);
    }

    //构造函数，传入执行器
    public SQL(Executor executor) {
        createExecutor(executor);
    }

    //传入执行器
    public SQL(NamedParameterJdbcTemplate template) {
        executor = new JdbcTemplateExecutor(template);
    }


    //克隆
    protected SQL(SQL sql) {
        this.builder = sql.builder.clone();
        this.jdbcModel.mergeParameterMap(sql);
        this.executor = sql.executor;
    }

    public SQL clone() {
        return new SQL(this);
    }

    //默认执行器为JdbcTemplateMapper
    public SQL createExecutor() {
        executor = DbTools.getExecutor();
        return this;
    }

    public SQL createExecutor(Connection connection) {

        executor = new DbUtilsExecutor(connection);
        return this;

    }

    public SQL createExecutor(DataSource dataSource) {
        executor = new DbUtilsExecutor(dataSource);
        return this;
    }

    public SQL createExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    //大学，构造，并传入执行器
    public static SQL ofExecutor(Connection connection) {
        return new SQL(connection);
    }

    public static SQL ofExecutor(DataSource dataSource) {
        return new SQL(dataSource);
    }

    public static SQL ofExecutor(Executor executor) {
        return new SQL(executor);

    }

    //小写，设置执行器
    public SQL executor(Connection connection) {
        createExecutor(connection);
        return this;
    }

    public SQL executor(DataSource dataSource) {
        createExecutor(dataSource);
        return this;
    }

    public SQL executor(Executor executor) {
        createExecutor(executor);

        return this;
    }

    //静态工厂方法-----------------------------------------------------------------------------------
    public static SQL SELECT(String table) {
        return new SQL().select(table);
    }

    public static SQL SELECT(Class<?> clazz) {
        return SELECT(TableNameResolver.getTableName(clazz));
    }

    public static SQL SELECT(SQL subSql, String alias) {
        return new SQL().select(subSql, alias);
    }

    public static SQL UPDATE(String table) {
        return new SQL().update(table);
    }

    public static SQL UPDATE(Class<?> clazz) {
        return UPDATE(TableNameResolver.getTableName(clazz));
    }

    public static SQL DELETE(String table) {
        return new SQL().delete(table);
    }

    public static SQL DELETE(Class<?> clazz) {
        return DELETE(TableNameResolver.getTableName(clazz));
    }

    public static SQL INSERT(String table) {
        return new SQL().insert(table);
    }

    public static SQL INSERT(Class<?> clazz) {
        return INSERT(TableNameResolver.getTableName(clazz));
    }

    public static SQL WHERE() {
        return new SQL().where();
    }

    public static SQL WHERE(String name) {
        return new SQL().where().where(name);
    }

    public static SQL WHERE(SQL sql) {
        return new SQL().where().where(sql);
    }

    public static SQL ADDSQL(String directSql, Object... values) {
        return new SQL().addSql(directSql, values);
    }
    public static <T>  SQL ADDSQL(PropertyGetter<T> getter, Object... values) {

        return ADDSQL(LambdaExtractor.getColumnName(getter), values);

    }
    public static <T> SQL ADDSQLFullColumn(PropertyGetter<T> getter, Object... values) {

        return ADDSQL(LambdaExtractor.getFullColumnName(getter), values);

    }

    //开始任务-----------------------------------------------------------
    public SQL select(String table) {
        this.builder.from(table);
        return this;
    }

    public SQL select(Class<?> clazz) {
        return select(TableNameResolver.getTableName(clazz));
    }

    public SQL select(SQL subSql, String alias) {
        return this.from(subSql, alias);
    }

    public SQL update(String table) {
        this.builder.update(table);
        return this;
    }

    public SQL update(Class<?> clazz) {
        return update(TableNameResolver.getTableName(clazz));
    }

    public SQL delete(String table) {
        this.builder.delete(table);
        return this;
    }

    public SQL delete(Class<?> clazz) {
        return delete(TableNameResolver.getTableName(clazz));
    }

    public SQL insert(String table) {
        this.builder.insert(table);
        return this;
    }

    public SQL insert(Class<?> clazz) {
        return insert(TableNameResolver.getTableName(clazz));
    }

    public SQL where() {
        this.builder.where();
        return this;
    }
    //初始化----------------------------------------

    public SQL setColumn(String name) {

        this.builder.setColumn(name);
        return this;
    }

    public SQL setWhere(String name) {

        this.builder.setWhere(name);
        return this;
    }

    //查询表----------------------------------------
    public SQL from(String table) {
        this.builder.from(table);
        return this;
    }

    public SQL from(Class<?> clazz) {
        return from(TableNameResolver.getTableName(clazz));
    }

    //子查询表
    public SQL from(SQL subSql, String alias) {

        this.builder.from(jdbcModel.processSqlName(subSql) + alias + " ");
        return this;
    }


    public SQL where(String name) {
        this.builder.where(name);
        return this;
    }

    public SQL where(SQL subSql) {
        if (subSql == null)
            return this;

        this.builder.where(jdbcModel.processSqlName(subSql));
        return this;
    }
// 分组条件-----
    public SQL having(String name) {
        this.builder.having(name);
        return this;
    }

    public SQL having(SQL subSql) {

        this.builder.having(jdbcModel.processSqlName(subSql));
        return this;
    }

    //直接给出sql和参数名称，后面用set设置值配合使用--------------------------------------------------
    public SQL addSql(String directSql, Object... values) {

        this.builder.setDirectSql(jdbcModel.createSqlNameParams(directSql, values));
        return this;
    }
    public <T> SQL addSql(PropertyGetter<T> getter, Object... values) {

        this.addSql(LambdaExtractor.getColumnName(getter), values);
        return this;
    }
    public <T> SQL addSqlFullColumn(PropertyGetter<T> getter, Object... values) {

        this.addSql(LambdaExtractor.getFullColumnName(getter), values);
        return this;
    }
    public SQL addSql(SQL subSql) {

        this.builder.setDirectSql(jdbcModel.processSqlName(subSql));
        return this;
    }

    public JdbcModel getJdbcDataModel() {
        return jdbcModel;
    }

    public void setSqlAndParameter(JdbcModel jdbcModel) {
        this.jdbcModel = jdbcModel;
    }

    //代理SqlAndParameter---------------------------
    public Map<String, Object> getParameterMap() {
        return jdbcModel.getParameterMap();
    }

    public Object[] getParamsList() {
        return jdbcModel.getParamsList().toArray();
    }

    public String getJdbcSql() {
        this.toString();
        return jdbcModel.getJdbcSql();
    }

    public SQL distinct() {
        this.builder.distinct();
        return this;
    }
//分组-----groupBy---

    public SQL groupBy(String name) {
        this.builder.groupBy(name);
        return this;
    }

    public <T> SQL groupBy(PropertyGetter<T> getter) {
        return groupBy(getter, false);
    }

    public <T> SQL groupByFullColumn(PropertyGetter<T> getter) {
        return groupBy(getter, true);
    }

    public <T> SQL groupBy(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return groupBy(LambdaExtractor.getFullColumnName(getter));
        else
            return groupBy(LambdaExtractor.getColumnName(getter));
    }

    //----------多表连接------------------------------
    public SQL join(String join) {
        this.builder.join(join);
        return this;
    }

    public SQL join(String table, String on) {
        this.join("inner join", table, on);
        return this;
    }
    public SQL join(String table, SQL  sql) {

        return this.join(table,sql.toString());
    }
    public SQL join(Class<?> clazz, String on) {
        return join(TableNameResolver.getTableName(clazz), on);
    }

    public <T> SQL join(Class<?> clazz, PropertyGetter<T> leftGetter, PropertyGetter<T> rightGetter) {
        String leftColumnReference = LambdaExtractor.getFullColumnName(leftGetter);
        String rightColumnReference = LambdaExtractor.getFullColumnName(rightGetter);

        String onCondition = String.format("%s = %s", leftColumnReference, rightColumnReference);
        return join(TableNameResolver.getTableName(clazz), onCondition);
    }

    public SQL leftJoin(String table, String on) {
        this.join("left join", table, on);
        return this;
    }

    public SQL leftJoin(Class<?> clazz, String on) {
        return leftJoin(TableNameResolver.getTableName(clazz), on);
    }

    public SQL rightJoin(String table, String on) {
        this.join("right join", table, on);
        return this;
    }

    public SQL rightJoin(Class<?> clazz, String on) {
        return rightJoin(TableNameResolver.getTableName(clazz), on);
    }

    public SQL fullJoin(String table, String on) {
        this.join("full join", table, on);
        return this;
    }

    public SQL fullJoin(Class<?> clazz, String on) {
        return rightJoin(TableNameResolver.getTableName(clazz), on);
    }

    public SQL join(String join, String table, String on) {
        if (on != null && on.trim().length() != 0) {
            this.builder.join(join + " " + table + " on " + on);
        } else
            this.builder.join(join + " " + table);
        return this;
    }

    //-----------------------排序----------------------------------------------------
    //---多组排序
    public SQL orderByAsc(List<String> names) {
        for (String name : names) {
            orderByAsc(name);
        }

        return this;
    }

    public SQL orderByDesc(List<String> names) {
        for (String name : names) {
            orderByDesc(name);
        }

        return this;
    }

    //---升序排序
    public SQL orderByAsc(String name) {

        this.orderBy(name, true);
        return this;
    }

    public <T> SQL orderByAsc(PropertyGetter<T> getter) {
        return orderByAsc(getter, false);
    }

    public <T> SQL orderByAscFullColumn(PropertyGetter<T> getter) {
        return orderByAsc(getter, true);
    }

    public <T> SQL orderByAsc(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return orderByAsc(LambdaExtractor.getFullColumnName(getter));
        else
            return orderByAsc(LambdaExtractor.getColumnName(getter));
    }

    //---降序排序
    public SQL orderByDesc(String name) {
        this.orderBy(name, false);
        return this;
    }


    public <T> SQL orderByDesc(PropertyGetter<T> getter) {
        return orderByDesc(getter, false);
    }

    public <T> SQL orderByDescFullColumn(PropertyGetter<T> getter) {
        return orderByDesc(getter, true);
    }

    public <T> SQL orderByDesc(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return orderByDesc(LambdaExtractor.getFullColumnName(getter));
        else
            return orderByDesc(LambdaExtractor.getColumnName(getter));
    }

    //---默认升序排序
    public SQL orderBy(String name) {
        this.orderBy(name, true);
        return this;
    }

    public <T> SQL orderBy(PropertyGetter<T> getter) {
        return orderBy(getter, false);
    }

    public <T> SQL orderByFullColumn(PropertyGetter<T> getter) {
        return orderBy(getter, true);
    }

    public <T> SQL orderBy(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return orderBy(LambdaExtractor.getFullColumnName(getter));
        else
            return orderBy(LambdaExtractor.getColumnName(getter));
    }

    //---排序
    public SQL orderBy(String name, boolean ascending) {
        this.builder.orderBy(name, ascending);
        return this;
    }


    public <T> SQL orderByColumn(PropertyGetter<T> getter, boolean ascending) {
        return orderBy(getter, ascending, false);
    }

    public <T> SQL orderByFullColumn(PropertyGetter<T> getter, boolean ascending) {
        return orderBy(getter, ascending, true);
    }

    public <T> SQL orderBy(PropertyGetter<T> getter, boolean ascending, boolean useFullColumn) {
        if (useFullColumn)
            return orderBy(LambdaExtractor.getFullColumnName(getter), ascending);
        else
            return orderBy(LambdaExtractor.getColumnName(getter), ascending);
    }

    public SqlModel getBuilder() {
        return this.builder;
    }


    //代理SqlDataModel---------------------------
//------------------------------聚合函数列----------------------------------------
    //---sum--------------
    public SQL sum(String name) {
        return aggregate("sum", name);
    }

    public <T> SQL sum(PropertyGetter<T> getter) {
        return sum(getter, false);
    }

    public <T> SQL sumFullColumn(PropertyGetter<T> getter) {
        return sum(getter, true);
    }

    public <T> SQL sum(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return sum(LambdaExtractor.getFullColumnName(getter));
        else
            return sum(LambdaExtractor.getColumnName(getter));
    }

    //---sum-----alias---------
    public SQL sum(String name, String alias) {
        return aggregate("sum", name, alias);
    }

    public <T> SQL sum(PropertyGetter<T> getter, String alias) {
        return sum(getter, alias, false);
    }

    public <T> SQL sumFullColumn(PropertyGetter<T> getter, String alias) {
        return sum(getter, alias, true);
    }

    public <T> SQL sum(PropertyGetter<T> getter, String alias, boolean useFullColumn) {
        if (useFullColumn)
            return sum(LambdaExtractor.getFullColumnName(getter), alias);
        else
            return sum(LambdaExtractor.getColumnName(getter), alias);
    }

    //---avg--------------
    public SQL avg(String name) {
        return aggregate("avg", name);
    }


    public <T> SQL avg(PropertyGetter<T> getter) {
        return avg(getter, false);
    }

    public <T> SQL avgFullColumn(PropertyGetter<T> getter) {
        return avg(getter, true);
    }

    public <T> SQL avg(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return avg(LambdaExtractor.getFullColumnName(getter));
        else
            return avg(LambdaExtractor.getColumnName(getter));
    }

    //---avg-----alias---------
    public SQL avg(String name, String alias) {
        return aggregate("avg", name, alias);
    }

    public <T> SQL avg(PropertyGetter<T> getter, String alias) {
        return avg(getter, alias, false);
    }

    public <T> SQL avgFullColumn(PropertyGetter<T> getter, String alias) {
        return sum(getter, alias, true);
    }

    public <T> SQL avg(PropertyGetter<T> getter, String alias, boolean useFullColumn) {
        if (useFullColumn)
            return sum(LambdaExtractor.getFullColumnName(getter), alias);
        else
            return sum(LambdaExtractor.getColumnName(getter), alias);
    }

    //---max--------------
    public SQL max(String name) {
        return aggregate("max", name);
    }


    public <T> SQL max(PropertyGetter<T> getter) {
        return max(getter, false);
    }

    public <T> SQL maxFullColumn(PropertyGetter<T> getter) {
        return max(getter, true);
    }

    public <T> SQL max(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return max(LambdaExtractor.getFullColumnName(getter));
        else
            return max(LambdaExtractor.getColumnName(getter));
    }

    //---max--alis------------
    public SQL max(String name, String alias) {
        return aggregate("max", name, alias);
    }


    public <T> SQL max(PropertyGetter<T> getter, String alias) {
        return max(getter, alias, false);
    }

    public <T> SQL maxFullColumn(PropertyGetter<T> getter, String alias) {
        return max(getter, alias, true);
    }

    public <T> SQL max(PropertyGetter<T> getter, String alias, boolean useFullColumn) {
        if (useFullColumn)
            return max(LambdaExtractor.getFullColumnName(getter), alias);
        else
            return max(LambdaExtractor.getColumnName(getter), alias);
    }

    //---min--------------
    public SQL min(String name) {
        return aggregate("min", name);
    }


    public <T> SQL min(PropertyGetter<T> getter) {
        return min(getter, false);
    }

    public <T> SQL minFullColumn(PropertyGetter<T> getter) {
        return min(getter, true);
    }

    public <T> SQL min(PropertyGetter<T> getter, boolean useFullColumn) {
        if (useFullColumn)
            return min(LambdaExtractor.getFullColumnName(getter));
        else
            return min(LambdaExtractor.getColumnName(getter));
    }

    //---min--alis------------
    public SQL min(String name, String alias) {
        return aggregate("min", name, alias);
    }


    public <T> SQL min(PropertyGetter<T> getter, String alias) {
        return min(getter, alias, false);
    }

    public <T> SQL minFullColumn(PropertyGetter<T> getter, String alias) {
        return min(getter, alias, true);
    }

    public <T> SQL min(PropertyGetter<T> getter, String alias, boolean useFullColumn) {
        if (useFullColumn)
            return min(LambdaExtractor.getFullColumnName(getter), alias);
        else
            return min(LambdaExtractor.getColumnName(getter), alias);
    }

    //---count--------------
    public SQL count() {
        this.builder.column("count(" + "*" + ") ");
        return this;
    }

    //---count--alis------------
    public SQL count(String alias) {
        this.builder.column("count(" + "*" + ") " + alias + " ");
        return this;
    }

    //---aggregate--------------
    public SQL aggregate(String function, String name) {
        return aggregate(function, name, null);
    }

    public <T> SQL aggregate(String function, PropertyGetter<T> getter) {
        return aggregate(function, getter, null, false);
    }

    public <T> SQL aggregateFullColumn(String function, PropertyGetter<T> getter) {
        return aggregate(function, getter, null, true);
    }

    public <T> SQL aggregate(String function, PropertyGetter<T> getter, boolean useFullColumn) {
        return aggregate(function, getter, null, useFullColumn);
    }

    //---aggregate--alis------------
    public SQL aggregate(String function, String name, String alias) {
        String alias1 = alias;
        if (alias1 == null)
            alias1 = name;
        this.builder.column(" " + function + "(" + name + ") " + alias1 + " ");

        return this;
    }

    public <T> SQL aggregate(String function, PropertyGetter<T> getter, String alias) {
        return aggregate(function, getter, alias, false);
    }

    public <T> SQL aggregateFullColumn(String function, PropertyGetter<T> getter, String alias) {
        return aggregate(function, getter, alias, true);
    }

    public <T> SQL aggregate(String function, PropertyGetter<T> getter, String alias, boolean useFullColumn) {
        if (useFullColumn)
            return aggregate(function, LambdaExtractor.getFullColumnName(getter), alias);
        else
            return aggregate(function, LambdaExtractor.getColumnName(getter), alias);
    }

    //-------------------------查询列----------------------------------------------------
    public SQL column(String name) {
        this.builder.column(name);
        return this;
    }

    public <T> SQL column(PropertyGetter<T> getter) {
        return column(LambdaExtractor.getColumnName(getter));
    }

    public <T> SQL fullColumn(PropertyGetter<T> getter) {
        return column(LambdaExtractor.getFullColumnName(getter));
    }

    public <T> SQL column(PropertyGetter<T>... getters) {
        for (int i = 0; i < getters.length; i++) {
            column(LambdaExtractor.getColumnName(getters[i]));
        }
        return this;
    }

    public <T> SQL fullColumn(PropertyGetter<T>... getters) {
        for (int i = 0; i < getters.length; i++) {
            column(LambdaExtractor.getFullColumnName(getters[i]));
        }
        return this;
    }

    //---列别名-----------------------
    public SQL column(String name, String alias) {
        this.builder.column(name, alias);
        return this;
    }

    public <T> SQL column(PropertyGetter<T> getter, String alias) {
        return column(LambdaExtractor.getColumnName(getter), alias);
    }

    public <T> SQL fullColumn(PropertyGetter<T> getter, String alias) {
        return column(LambdaExtractor.getFullColumnName(getter), alias);
    }

    //---表.字段 列别名-----------------------
    public SQL column(String tableAlias, String name, String alias) {
        this.builder.column(tableAlias + "." + name, alias);
        return this;
    }

    public SQL column(Class<?> clazz, String name, String alias) {
        this.builder.column(TableNameResolver.getTableName(clazz) + "." + name, alias);
        return this;
    }

    public <T> SQL column(String tableAlias, PropertyGetter<T> getter, String alias) {
        return column(tableAlias, LambdaExtractor.getColumnName(getter), alias);
    }

    public <T> SQL column(Class<?> clazz, PropertyGetter<T> getter, String alias) {
        return column(TableNameResolver.getTableName(clazz), LambdaExtractor.getColumnName(getter), alias);
    }

    //---列加分组-----------------------
    public SQL column(String name, boolean groupBy) {
        this.builder.column(name, groupBy);
        return this;
    }

    public <T> SQL column(PropertyGetter<T> getter, boolean groupBy) {
        return column(LambdaExtractor.getColumnName(getter), groupBy);
    }

    public <T> SQL fullColumn(PropertyGetter<T> getter, boolean groupBy) {
        return column(LambdaExtractor.getFullColumnName(getter), groupBy);
    }

    //子查询列----------------------------------------------------
    public SQL column(SQL subSql, String alias) {
        this.builder.column(jdbcModel.processSqlName(subSql) + alias + " ");
        return this;
    }

    //0、通用查询-----------age=18----------------------------------------------------


    //一、比较谓词----------------------------------------------------
    public SQL func(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    //一、比较谓词----------------------------------------------------
    public SQL where(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    public <T> SQL where(PropertyGetter<T> getter, Object value) {
        return where(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL eqMap(Map<String, Object> columnMap) {
        if (columnMap == null)
            return this;
        for (String key : columnMap.keySet()) {
            eq(key, columnMap.get(key));
        }
        return this;
    }

    public SQL eq(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    // 新增支持Lambda的eq方法
// 支持泛型的 eq 方法<T> String getColumnName(PropertyGetter<T> getter)
    public <T> SQL eq(PropertyGetter<T> getter, Object value) {
        return eq(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL eqIfNotNull(Object name, Object value) {
        if (value == null)
            return this;
        return nameOperatorValue(name, "=", value);
    }

    public <T> SQL eqIfNotNull(PropertyGetter<T> getter, Object value) {
        return eqIfNotNull(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL neq(Object name, Object value) {
        return nameOperatorValue(name, "<>", value);
    }

    public <T> SQL neq(PropertyGetter<T> getter, Object value) {
        return neq(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL gt(Object name, Object value) {
        return nameOperatorValue(name, ">", value);
    }

    public <T> SQL gt(PropertyGetter<T> getter, Object value) {
        return gt(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL gte(Object name, Object value) {
        return nameOperatorValue(name, ">=", value);
    }

    public <T> SQL gte(PropertyGetter<T> getter, Object value) {
        return gte(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL lt(Object name, Object value) {
        return nameOperatorValue(name, "<", value);
    }

    public <T> SQL lt(PropertyGetter<T> getter, Object value) {
        return lt(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL lte(Object name, Object value) {
        return nameOperatorValue(name, "<=", value);
    }

    public <T> SQL lte(PropertyGetter<T> getter, Object value) {
        return lte(LambdaExtractor.getColumnName(getter), value);
    }

    //name+operator+value
    protected SQL nameOperatorValue(Object name, String operator, Object value) {
        if (value == null)
            return this;

        this.where(String.format(" %s %s %s ", jdbcModel.processSqlName(name), operator, jdbcModel.processSqlValue(value)));
        return this;
    }

    //二、LIKE 谓词——字符串的部分一致查询
    public SQL like(Object name, Object value) {
        return likeOperator(name, "like", value);
    }

    public <T> SQL like(PropertyGetter<T> getter, Object value) {
        return like(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL like_(Object name, Object value) {
        return likeOperator(name, "like_", value);
    }

    public <T> SQL like_(PropertyGetter<T> getter, Object value) {
        return like_(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL notLike(Object name, Object value) {
        return likeOperator(name, "notLike", value);
    }

    public <T> SQL notLike(PropertyGetter<T> getter, Object value) {
        return notLike(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL likeLeft(Object name, Object value) {
        return likeOperator(name, "likeLeft", value);
    }

    public <T> SQL likeLeft(PropertyGetter<T> getter, Object value) {
        return likeLeft(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL likeRight(Object name, Object value) {
        return likeOperator(name, "likeRight", value);
    }

    public <T> SQL likeRight(PropertyGetter<T> getter, Object value) {
        return likeRight(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL notLikeLeft(Object name, Object value) {
        return likeOperator(name, "notLikeLeft", value);
    }

    public <T> SQL notLikeLeft(PropertyGetter<T> getter, Object value) {
        return notLikeLeft(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL notLikeRight(Object name, Object value) {
        return likeOperator(name, "notLikeRight", value);
    }

    public <T> SQL notLikeRight(PropertyGetter<T> getter, Object value) {
        return notLikeRight(LambdaExtractor.getColumnName(getter), value);
    }

    protected SQL likeOperator(Object name, String operator, Object value) {

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
        return this;
    }

    //三、BETWEEN 谓词——范围查询
    public SQL between(Object name, Object value1, Object value2) {
        this.between(name, "  between ", value1, value2);

        return this;
    }

    public <T> SQL between(PropertyGetter<T> getter, Object value1, Object value2) {
        return between(LambdaExtractor.getColumnName(getter), value1, value2);
    }

    public SQL notBetween(Object name, Object value1, Object value2) {

        this.between(name, "  not between ", value1, value2);

        return this;
    }

    public <T> SQL notBetween(PropertyGetter<T> getter, Object value1, Object value2) {
        return notBetween(LambdaExtractor.getColumnName(getter), value1, value2);
    }

    protected SQL between(Object name, String between, Object value1, Object value2) {
        String placehoder1 = jdbcModel.processSqlValue(value1);
        String placehoder2 = jdbcModel.processSqlValue(value2);
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + between + placehoder1 + " and " + placehoder2);

        return this;
    }

    //四、IS NULL、IS NOT NULL——判断是否为 NULL

    public SQL isNull(Object name) {

        this.isNull(name, "is  null");
        return this;
    }

    public <T> SQL isNull(PropertyGetter<T> getter) {
        return isNull(LambdaExtractor.getColumnName(getter));
    }

    public SQL isNotNull(Object name) {

        this.isNull(name, "is not null");
        return this;
    }

    public <T> SQL isNotNull(PropertyGetter<T> getter) {
        return isNotNull(LambdaExtractor.getColumnName(getter));
    }

    protected SQL isNull(Object name, String isNull) {
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + " " + isNull);
        return this;
    }

    //五、IN 谓词——OR 的简便用法
    public SQL or() {

        this.where(" or ");
        return this;
    }
    public SQL and() {

        this.where(" and ");
        return this;
    }
    public SQL in(Object name, List<?> values) {
        return nameOperatorValue(name, "in", values);

    }

    public <T> SQL in(PropertyGetter<T> getter, List<?> values) {
        return in(LambdaExtractor.getColumnName(getter), values);
    }

    public SQL in(Object name, Object... values) {

        return nameOperatorValue(name, "in", new ArrayList<>(Arrays.asList(values)));
    }

    public <T> SQL in(PropertyGetter<T> getter, Object... values) {
        return in(LambdaExtractor.getColumnName(getter), values);
    }

    public SQL in(Object name, SQL subSql) {

        return nameOperatorValue(name, "in", subSql);
    }

    public <T> SQL in(PropertyGetter<T> getter, SQL subSql) {
        return in(LambdaExtractor.getColumnName(getter), subSql);
    }

    public SQL notIn(Object name, List<?> values) {
        return nameOperatorValue(name, "not in", values);
    }

    public <T> SQL notIn(PropertyGetter<T> getter, List<?> values) {
        return notIn(LambdaExtractor.getColumnName(getter), values);
    }

    public SQL notIn(Object name, SQL subSql) {

        return nameOperatorValue(name, "not in", subSql);
    }

    public <T> SQL notIn(PropertyGetter<T> getter, SQL subSql) {
        return notIn(LambdaExtractor.getColumnName(getter), subSql);
    }

    public SQL notIn(Object name, Object... values) {
        return nameOperatorValue(name, "not in", new ArrayList<>(Arrays.asList(values)));

    }

    public <T> SQL notIn(PropertyGetter<T> getter, Object... values) {
        return notIn(LambdaExtractor.getColumnName(getter), values);
    }

    //七、EXIST 谓词
    //
    public SQL exists(SQL subSQL) {
        return operatorValue("exists", subSQL);
    }

    public SQL notExists(SQL subSQL) {
        return operatorValue("not exists", subSQL);
    }

    //operator+value------exists（value）-------
    protected SQL operatorValue(String operator, SQL subSQL) {
        this.where(String.format("  %s %s ", operator, jdbcModel.processSqlValue(subSQL)));
        return this;
    }

    //八、括号
    public SQL begin() {

        this.where(" ( ");
        return this;
    }

    public SQL end() {

        this.where(" ) ");
        return this;
    }




    //-------------------------------更新设置--------------------------------------------


    //直接遍历字段，然后设置
    public SQL setMap(String fields, Map value) {

        //表示不通过fields来过滤字段，直接用Map中的key，vaule的值都是有用的
        if (fields == null || fields.equals("") || fields.equals("*")) {
            return setMap(value);
        }
        //通过fields来过滤Map的key,value中包含没用的key
        else {
            String[] parts = fields.split(",\\s*");
            for (int i = 0; i < parts.length; i++) {
                //通过值过滤，存在值就更新，不存在不更新，双过滤
                if (value.containsKey(parts[i]))
                    set(parts[i], new DbParameter(parts[i], value.get(parts[i])));
            }
            return this;
        }

    }

    //直接用Map中的key，vaule的值都是有用的
    public SQL setMap(Map<String, Object> columnMap) {
        for (String key : columnMap.keySet()) {
            set(key, new DbParameter(key, columnMap.get(key), null));

        }
        return this;
    }

    //设置字段---值---------------------------------------------
    public SQL set(String name, Object value) {

        set(name, new DbParameter(name, value));
        return this;
    }

    public <T> SQL set(PropertyGetter<T> getter, Object value) {
        return set(LambdaExtractor.getColumnName(getter), value);
    }

    //不为空时候更新
    public SQL setIfNotNull(String name, Object value) {
        if (value == null)
            return this;

        set(name, new DbParameter(name, value));
        return this;
    }

    public <T> SQL setIfNotNull(PropertyGetter<T> getter, Object value) {
        return setIfNotNull(LambdaExtractor.getColumnName(getter), value);
    }

    public SQL set(String name, Object value, String datatype) {
        set(name, new DbParameter(name, value, datatype));
        return this;
    }

    public <T> SQL set(PropertyGetter<T> getter, Object value, String datatype) {
        return set(LambdaExtractor.getColumnName(getter), value, datatype);
    }

    public SQL set(String name, Object value, String datatype, boolean allowNull) {
        set(name, new DbParameter(name, value, datatype, allowNull));
        return this;
    }

    public <T> SQL set(PropertyGetter<T> getter, Object value, String datatype, boolean allowNull) {
        return set(LambdaExtractor.getColumnName(getter), value, datatype, allowNull);
    }

    public SQL set(String name, DbParameter pmt) {
        set1(name, pmt.getValue());
        return this;

    }

    public <T> SQL set(PropertyGetter<T> getter, DbParameter pmt) {
        return set(LambdaExtractor.getColumnName(getter), pmt);
    }

    protected SQL set1(String name, Object subSql) {
        //设置占位符号
        setPlaceholder(name, jdbcModel.processSqlValue(subSql));

        return this;
    }

    private void setPlaceholder(String name, String nameParamPlaceholder) {

        //更新时候，存set
        if (builder.getDoState().equals("update")) {
            this.builder.set(" " + name + " = " + nameParamPlaceholder);
        }
        //增加时候，set存列与值
        else if (builder.getDoState().equals("insert")) {


            this.builder.insertColumn(name, nameParamPlaceholder);

        }

    }

    //参数设置--------------------------------------------

    public SQL setValue(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlValue(subSql));
        return this;
    }

    public <T> SQL setValue(PropertyGetter<T> getter, Object subSql) {
        return setValue(LambdaExtractor.getColumnName(getter), subSql);
    }

    public SQL setValue$(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlName(subSql));
        return this;
    }

    public <T> SQL setValue$(PropertyGetter<T> getter, Object subSql) {
        return setValue$(LambdaExtractor.getColumnName(getter), subSql);
    }

    public String toSelect() {
        return this.builder.toSelect();
    }

    public String toUpdate() {
        return this.builder.toUpdate();
    }

    public String toDelete() {
        return this.builder.toDelete();
    }

    public String toInsert() {
        return this.builder.toInsert();
    }

    //输出sql------------------------------------------
    public String toString() {

        String sql1 = this.builder.toString();

        jdbcModel.createJdbcSqlFromNameSql(sql1);
        return sql1;
    }

    //自我扩展-------------------------------------------------------------------------------------------------
    public SQL last(Object last) {
        return new SQL(this.executor)
                .addSql(" :arg0  :arg1")
                .setValue$("arg0", this)
                .setValue$("arg1", last);
    }

    public SQL union(SQL subSql) {
        return union("union", subSql);
    }

    public SQL unionAll(SQL subSql) {
        return union("union all", subSql);
    }

    public SQL union(String union, SQL subSql) {
        return new SQL(this.executor).from(
                SQL.ADDSQL(" :arg0 " + union + " :arg1")
                        .setValue("arg0", this)
                        .setValue("arg1", subSql), "a");
    }

    public SQL forUpdate() {
        last("for update");
        return this;
    }

    public SQL noWait() {
        last("for update nowait");
        return this;
    }


    //代理-------------------------------------------------执行类
    public void toExecutor() {
        this.toString();
    }


    //更新数据库----------------------------------------------------------------------------------------------------


    //更新
    public int update() {
        return executor.update(this);
    }

    //增加，返回主键
    public Number insert() {
        return executor.insert(this);
    }

    //执行存储过程
    public int execute() {

        return executor.execute(this);
    }

    //查询数据库-----------------------------------------------------------------------------------

    //返回单列单行数据
    public String getString() {
        return executor.getString(this);
    }


    public Integer getInteger() {
        return executor.getInteger(this);
    }

    public Long getLong() {
        return executor.getLong(this);
    }


    public Float getFloat() {
        return executor.getFloat(this);
    }

    public Double getDouble() {

        return executor.getDouble(this);
    }

    public Number getNumber() {
        return executor.getNumber(this);
    }


    public BigDecimal getBigDecimal() {

        return executor.getBigDecimal(this);
    }

    public Date getDate() {

        return executor.getDate(this);
    }


    public <T> T getValue(Class<T> requiredType) {
        return executor.getValue(this, requiredType);
    }

    //返回单列list数据
    public List<String> getStrings() {

        return executor.getStrings(this);
    }


    public List<Integer> getIntegers() {
        return executor.getIntegers(this);
    }


    public List<Long> getLongs() {

        return executor.getLongs(this);
    }


    public List<Double> getDoubles() {
        return executor.getDoubles(this);
    }


    public List<Float> getFloats() {
        return executor.getFloats(this);
    }


    public List<Number> getNumbers() {

        return executor.getNumbers(this);
    }


    public List<BigDecimal> getBigDecimals() {

        return executor.getBigDecimals(this);
    }


    public List<Date> getDates() {

        return executor.getDates(this);
    }


    public <T> List<T> getValues(Class<T> requiredType) {

        return executor.getValues(this, requiredType);
    }


    //返回单行数据

    public Map<String, Object> getMap() {

        return executor.getMap(this);
    }


    //返回多行数据
    public List<Map<String, Object>> getMaps() {

        return executor.getMaps(this);
    }


    //返回Bean实体
    public <T> T getBean(Class<T> T) {

        return executor.getBean(this, T);
    }


    //返回Bean list
    public <T> List<T> getBeans(Class<T> T) {

        return executor.getBeans(this, T);
    }


    //查询应用-------------判断是否存在------------------------------------------------
    public boolean isExists() {
        return this.executor.isExists(this);
    }

    //对一个查询给出记录数
    public Number getCount() {
        return this.executor.getCount(this);
    }


}
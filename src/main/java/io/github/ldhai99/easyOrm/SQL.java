package io.github.ldhai99.easyOrm;


import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;

import io.github.ldhai99.easyOrm.builder.ExecutorHandler;
import io.github.ldhai99.easyOrm.executor.DbUtilsExecutor;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import io.github.ldhai99.easyOrm.model.JdbcModel;
import io.github.ldhai99.easyOrm.model.SqlModel;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;


public class SQL  extends ExecutorHandler<SQL> {
    private static final long serialVersionUID = 1L;



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
    // 基础初始化
    private void initComponents() {
        this.builder = new SqlModel();
        this.jdbcModel = new JdbcModel();
    }
    //构造方法
    public SQL() {
        initComponents();
        createExecutor();
    }

    //构造函数，传入连接，传入数据源
    public SQL(Connection connection) {
        this(); // 复用基础初始化
        createExecutor(connection);
    }

    //构造函数，传入数据源
    public SQL(DataSource dataSource) {
        this();
        createExecutor(dataSource);
    }

    //构造函数，传入执行器
    public SQL(Executor executor) {
        this();
        createExecutor(executor);
    }

    //传入执行器
    public SQL(NamedParameterJdbcTemplate template) {
        this();
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

    public static SQL WHERE(String clause) {
        return new SQL().where().where(clause);
    }

    public static SQL WHERE(SQL sql) {
        return new SQL().where().where(sql);
    }

    public static SQL FRAGMENT(String DynamicSql, Object... values) {
        return new SQL().append(DynamicSql, values);
    }
    public static SQL FRAGMENT(SQL subSql) {
        return new SQL().append(subSql);
    }
    public static <T>  SQL FRAGMENT(PropertyGetter<T> getter) {

        return FRAGMENT(FieldResolver.fullField(getter));

    }
    public static <T> SQL FRAGMENTColumn(PropertyGetter<T> getter) {

        return new SQL().append(FieldResolver.field(getter));

    }


    //直接给出sql和参数名称，后面用set设置值配合使用--------------------------------------------------
    public SQL append(String DynamicSql, Object... values) {

        this.builder.setDynamicSql(jdbcModel.createSqlNameParams(DynamicSql, values));
        return this;
    }
    public <T> SQL append(PropertyGetter<T> getter) {

        this.append(FieldResolver.fullField(getter));
        return this;
    }
    public <T> SQL addColumn(PropertyGetter<T> getter) {

        this.append(FieldResolver.field(getter));
        return this;
    }
    public SQL append(SQL subSql) {

        this.builder.setDynamicSql(jdbcModel.processSqlName(subSql));
        return this;
    }

    public SQL distinct() {
        this.builder.distinct();
        return this;
    }



    //自我扩展-------------------------------------------------------------------------------------------------

//    public SQL last(Object last) {
//        return new SQL(this.executor)
//                .append(" :arg0  :arg1")
//                .setValue$("arg0", this)
//                .setValue$("arg1", last);
//    }

    public SQL union(SQL subSql) {
        return union("union", subSql);
    }

    public SQL unionAll(SQL subSql) {
        return union("union all", subSql);
    }

    public SQL union(String union, SQL subSql) {
        return new SQL(this.executor).from(
                SQL.FRAGMENT(" :arg0 " + union + " :arg1")
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




    //应用扩展------------------------------------------------


    //对一个查询给出记录数
    public Number getCount() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getCount(self());
    }
    public Number getCount(SQL sql)  {
        if (sql.getBuilder().hasGroup()) {
            SQL sql1 = SQL.SELECT(sql.clone().setColumn("count(*) count"), "a").setColumn("count(*) count");
            return sql1.getNumber();

        }else{
            SQL sql1 =sql.clone().setColumn("count(*) count");
            return sql1.getNumber();
        }
    }
    //查询应用-------------判断是否存在------------------------------------------------
    public boolean isExists() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return isExists(self());
    }
    public static boolean isExists( SQL ...sql )  {
        SQL sql1 =SQL.SELECT("dual").column("1").exists(sql[0]);
        for (int i=1;i<sql.length;i++){
            sql1.or().exists(sql[i]);
        }

        if (sql1.getMaps().size() >= 1)
            return true;
        else
            return false;
    }

}
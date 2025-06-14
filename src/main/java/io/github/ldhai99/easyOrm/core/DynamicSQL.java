package io.github.ldhai99.easyOrm.core;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.base.TaskType;
import io.github.ldhai99.easyOrm.builder.BaseSQL;
import io.github.ldhai99.easyOrm.builder.ExecutorHandler;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;

public class DynamicSQL extends ExecutorHandler<DynamicSQL> {
    //构造方法------------------------------------------------
    public DynamicSQL() {

        initComponents();
        createExecutor();
        this.builder.setTaskType(TaskType.DYNAMIC_SQL);
    }

    //构造函数，传入连接，传入数据源
    public DynamicSQL(Connection connection) {
        this(); // 复用基础初始化
        createExecutor(connection);
    }

    //构造函数，传入数据源
    public DynamicSQL(DataSource dataSource) {
        this();
        createExecutor(dataSource);
    }

    //构造函数，传入执行器
    public DynamicSQL(Executor executor) {
        this();
        createExecutor(executor);
    }

    //传入执行器
    public DynamicSQL(NamedParameterJdbcTemplate template) {
        this();
        executor = new JdbcTemplateExecutor(template);
    }
    //静态方法，传入sql和参数名称，后面用set设置值配合使用
    public static DynamicSQL of(String DynamicSql, Object... values) {
        return new DynamicSQL().append(DynamicSql, values);
    }
    public static DynamicSQL of(BaseSQL subSql) {
        return new DynamicSQL().append(subSql);
    }
    public static <T> DynamicSQL of(PropertyGetter<T> getter) {

        return of(FieldResolver.fullField(getter));

    }
    public static <T> DynamicSQL ofColumn(PropertyGetter<T> getter) {
        return new DynamicSQL().append(FieldResolver.field(getter));
    }


    //直接给出sql和参数名称，后面用set设置值配合使用--------------------------------------------------
    public DynamicSQL append(String DynamicSql, Object... values) {
        this.builder.setDynamicSql(jdbcModel.createSqlNameParams(DynamicSql, values));
        return this;
    }
    public <T> DynamicSQL append(PropertyGetter<T> getter) {
        this.append(FieldResolver.fullField(getter));
        return this;
    }
    public <T> DynamicSQL column(PropertyGetter<T> getter) {
        this.append(FieldResolver.field(getter));
        return this;
    }
    public DynamicSQL append(BaseSQL subSql) {
        this.builder.setDynamicSql(jdbcModel.processSqlName(subSql));
        return this;
    }
    public <T> DynamicSQL eq(PropertyGetter<T> getter) {
        this.append(FieldResolver.fullField(getter));
        return this;
    }
}

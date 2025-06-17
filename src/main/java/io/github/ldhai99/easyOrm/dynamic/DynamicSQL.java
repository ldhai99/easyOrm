package io.github.ldhai99.easyOrm.dynamic;

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
import java.util.Collection;

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
    // ============== 静态工厂方法 ADDSQL ============== //
    public static DynamicSQL ADDSQL() {
        return new DynamicSQL();
    }

    public static DynamicSQL ADDSQL(Connection conn) {
        return new DynamicSQL(conn);
    }

    public static DynamicSQL ADDSQL(DataSource ds) {
        return new DynamicSQL(ds);
    }

    public static DynamicSQL ADDSQL(Executor exec) {
        return new DynamicSQL(exec);
    }

    public static DynamicSQL ADDSQL(NamedParameterJdbcTemplate template) {
        return new DynamicSQL(template);
    }
    //静态工厂方法，传入sql和参数名称，后面用set设置值配合使用

    public static DynamicSQL ADDSQL(String DynamicSql, Object... values) {
        return new DynamicSQL().addSql(DynamicSql, values);
    }
    public static DynamicSQL ADDSQL(BaseSQL subSql) {
        return new DynamicSQL().addSql(subSql);
    }
    public static <T> DynamicSQL ADDSQL(PropertyGetter<T> getter) {

        return ADDSQL(FieldResolver.fullField(getter));

    }
    public static <T> DynamicSQL ADDColumn(PropertyGetter<T> getter) {
        return new DynamicSQL().addSql(FieldResolver.field(getter));
    }


    //直接给出sql和参数名称，后面用set设置值配合使用--------------------------------------------------
    public DynamicSQL addSql(String DynamicSql, Object... values) {
        this.builder.setDynamicSql(jdbcModel.createSqlNameParams(DynamicSql, values));
        return this;
    }
    public <T> DynamicSQL addSql(PropertyGetter<T> getter) {
        this.addSql(FieldResolver.fullField(getter));
        return this;
    }
    public <T> DynamicSQL column(PropertyGetter<T> getter) {
        this.addSql(FieldResolver.field(getter));
        return this;
    }
    public DynamicSQL addSql(BaseSQL subSql) {
        this.builder.setDynamicSql(jdbcModel.processSqlName(subSql));
        return this;
    }
    // ============== 操作符方法（单参数） ============== //

    /**
     * 添加等号操作符和字段名
     * 示例：.eq(User::getName) → " = user.name"
     */
    public <T> DynamicSQL eq(PropertyGetter<T> getter) {
        return addSql(" = " + FieldResolver.fullField(getter));
    }

    /**
     * 添加不等号操作符和字段名
     * 示例：.neq(User::getName) → " <> user.name"
     */
    public <T> DynamicSQL neq(PropertyGetter<T> getter) {
        return addSql(" <> " + FieldResolver.fullField(getter));
    }

    /**
     * 添加大于号操作符和字段名
     * 示例：.gt(User::getAge) → " > user.age"
     */
    public <T> DynamicSQL gt(PropertyGetter<T> getter) {
        return addSql(" > " + FieldResolver.fullField(getter));
    }

    /**
     * 添加大于等于号操作符和字段名
     * 示例：.gte(User::getAge) → " >= user.age"
     */
    public <T> DynamicSQL gte(PropertyGetter<T> getter) {
        return addSql(" >= " + FieldResolver.fullField(getter));
    }

    /**
     * 添加小于号操作符和字段名
     * 示例：.lt(User::getAge) → " < user.age"
     */
    public <T> DynamicSQL lt(PropertyGetter<T> getter) {
        return addSql(" < " + FieldResolver.fullField(getter));
    }

    /**
     * 添加小于等于号操作符和字段名
     * 示例：.lte(User::getAge) → " <= user.age"
     */
    public <T> DynamicSQL lte(PropertyGetter<T> getter) {
        return addSql(" <= " + FieldResolver.fullField(getter));
    }
    // ============== 完整条件方法 ============== //

    // 等于
    public DynamicSQL eq(String column, Object value) {
        return addSql(column + " = :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL eq(PropertyGetter<T> getter, Object value) {
        return eq(FieldResolver.fullField(getter), value);
    }

    // 不等于
    public DynamicSQL neq(String column, Object value) {
        return addSql(column + " <> :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL neq(PropertyGetter<T> getter, Object value) {
        return neq(FieldResolver.fullField(getter), value);
    }

    // 大于
    public DynamicSQL gt(String column, Object value) {
        return addSql(column + " > :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL gt(PropertyGetter<T> getter, Object value) {
        return gt(FieldResolver.fullField(getter), value);
    }

    // 大于等于
    public DynamicSQL gte(String column, Object value) {
        return addSql(column + " >= :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL gte(PropertyGetter<T> getter, Object value) {
        return gte(FieldResolver.fullField(getter), value);
    }

    // 小于
    public DynamicSQL lt(String column, Object value) {
        return addSql(column + " < :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL lt(PropertyGetter<T> getter, Object value) {
        return lt(FieldResolver.fullField(getter), value);
    }

    // 小于等于
    public DynamicSQL lte(String column, Object value) {
        return addSql(column + " <= :" + generateParamName(column))
                .bind(generateParamName(column), value);
    }

    public <T> DynamicSQL lte(PropertyGetter<T> getter, Object value) {
        return lte(FieldResolver.fullField(getter), value);
    }

    // LIKE
    public DynamicSQL like(String column, Object value) {
        return addSql(column + " LIKE :" + generateParamName(column))
                .bind(generateParamName(column), "%" + value + "%");
    }

    public <T> DynamicSQL like(PropertyGetter<T> getter, Object value) {
        return like(FieldResolver.fullField(getter), value);
    }

    // LIKE 前缀
    public DynamicSQL likeRight(String column, Object value) {
        return addSql(column + " LIKE :" + generateParamName(column))
                .bind(generateParamName(column), value + "%");
    }

    public <T> DynamicSQL likeRight(PropertyGetter<T> getter, Object value) {
        return likeRight(FieldResolver.fullField(getter), value);
    }

    // LIKE 后缀
    public DynamicSQL likeLeft(String column, Object value) {
        return addSql(column + " LIKE :" + generateParamName(column))
                .bind(generateParamName(column), "%" + value);
    }

    public <T> DynamicSQL likeLeft(PropertyGetter<T> getter, Object value) {
        return likeLeft(FieldResolver.fullField(getter), value);
    }

    // IS NULL
    public DynamicSQL isNull(String column) {
        return addSql(column + " IS NULL");
    }

    public <T> DynamicSQL isNull(PropertyGetter<T> getter) {
        return isNull(FieldResolver.fullField(getter));
    }

    // IS NOT NULL
    public DynamicSQL isNotNull(String column) {
        return addSql(column + " IS NOT NULL");
    }

    public <T> DynamicSQL isNotNull(PropertyGetter<T> getter) {
        return isNotNull(FieldResolver.fullField(getter));
    }

    // BETWEEN
    public DynamicSQL between(String column, Object start, Object end) {
        String param1 = generateParamName(column) + "_start";
        String param2 = generateParamName(column) + "_end";
        return addSql(column + " BETWEEN :" + param1 + " AND :" + param2)
                .bind(param1, start)
                .bind(param2, end);
    }

    public <T> DynamicSQL between(PropertyGetter<T> getter, Object start, Object end) {
        return between(FieldResolver.fullField(getter), start, end);
    }

    // IN
    public DynamicSQL in(String column, Collection<?> values) {
        return addSql(column + " IN (:" + generateParamName(column) + ")")
                .bind(generateParamName(column), values);
    }

    public <T> DynamicSQL in(PropertyGetter<T> getter, Collection<?> values) {
        return in(FieldResolver.fullField(getter), values);
    }

    // NOT IN
    public DynamicSQL notIn(String column, Collection<?> values) {
        return addSql(column + " NOT IN (:" + generateParamName(column) + ")")
                .bind(generateParamName(column), values);
    }

    public <T> DynamicSQL notIn(PropertyGetter<T> getter, Collection<?> values) {
        return notIn(FieldResolver.fullField(getter), values);
    }
    // ============== 辅助方法 ============== //
    private String generateParamName(String column) {
        // 生成合法的参数名（替换特殊字符）
        return column.replaceAll("[^a-zA-Z0-9]", "_");
    }
// ============== 实用链式方法 ============== //

    // 逻辑操作符
    public DynamicSQL and() {
        return addSql(" AND ");
    }

    public DynamicSQL or() {
        return addSql(" OR ");
    }

    // 分组
    public DynamicSQL begin() {
        return addSql(" ( ");
    }

    public DynamicSQL end() {
        return addSql(" ) ");
    }
    /**
     * 添加字段引用（含表别名）
     */
    public <T> DynamicSQL fld(PropertyGetter<T> getter) {
        return addSql(FieldResolver.fullField(getter));
    }

    /**
     * 添加纯列名（不含表别名）
     */
    public <T> DynamicSQL col(PropertyGetter<T> getter) {
        return addSql(FieldResolver.field(getter));
    }

    /**
     * 添加值占位符并绑定参数
     */
    public DynamicSQL val(Object value) {
        String paramName = "val_" + System.nanoTime();
        return addSql(":" + paramName).bind(paramName, value);
    }

    // 增强参数处理
    public DynamicSQL bind(String paramName, Object value) {
        setParameter(paramName, value);
        return this;
    }
}

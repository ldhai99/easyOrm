package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;
import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;
import io.github.ldhai99.easyOrm.base.TaskType;
import io.github.ldhai99.easyOrm.executor.DbUtilsExecutor;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.model.JdbcModel;
import io.github.ldhai99.easyOrm.model.SqlModel;
import io.github.ldhai99.easyOrm.tools.DbTools;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Map;

public abstract class BaseSQL <T extends BaseSQL<T>> implements Cloneable{

    protected SqlModel builder = new SqlModel();
    protected JdbcModel jdbcModel = new JdbcModel();


    //克隆

    // 克隆构造函数
//    public BaseSQL(BaseSQL<T> source) {
//        // 深拷贝 SqlModel
//        this.builder = source.builder.clone();
//
//        // 深拷贝 JdbcModel
//        this.jdbcModel = new JdbcModel();
//        this.jdbcModel.mergeParameterMap(source);
//
//        // 共享 Executor（通常不需要深拷贝）
//        this.executor = source.executor;
//    }

//克隆方法
    @SuppressWarnings("unchecked")
    public T clone() {
        try {
            // 创建新实例
            T clone = (T) super.clone();

            // 深拷贝 builder
            clone.builder = this.builder.clone();

            // 深拷贝 jdbcModel
            clone.jdbcModel = new JdbcModel();
            clone.jdbcModel.mergeParameterMap(this);

            // 共享 executor（通常不需要深拷贝）
            clone.executor = this.executor;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("BaseSQL should be cloneable", e);
        }
    }
    // 返回当前实例的泛型类型
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }
    public SqlModel getBuilder() {
        return this.builder;
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

    //开始任务-----------------------------------------------------------
    public T select(String table) {
        this.builder.from(table);
        return self();
    }

    public T select(Class<?> clazz) {
        return select(TableNameResolver.getTableName(clazz));
    }

    public T select(BaseSQL subSql, String alias) {
        return self().from(subSql, alias);
    }

    public T update(String table) {
        this.builder.update(table);
        return self();
    }

    public T update(Class<?> clazz) {
        return update(TableNameResolver.getTableName(clazz));
    }

    public T delete(String table) {
        this.builder.delete(table);
        return self();
    }

    public T delete(Class<?> clazz) {
        return delete(TableNameResolver.getTableName(clazz));
    }

    public T insert(String table) {
        this.builder.insert(table);
        return self();
    }

    public T insert(Class<?> clazz) {
        return insert(TableNameResolver.getTableName(clazz));
    }

    public T where() {
        this.builder.where();
        return self();
    }
    public T last(String clause) {
        this.builder.last(clause);
        return self();
    }
    //初始化----------------------------------------

    public T setColumn(String column) {

        this.builder.setColumn(column);
        return self();
    }
    public T setLast(String clause) {

        this.builder.setLast(clause);
        return self();
    }

    public T setWhere(String clause) {

        this.builder.setWhere(clause);
        return self();
    }
    //判断类型-----------------------------------------------------------------------------------
    public boolean isSelect() {
        return self().builder.isSelect();
    }
    public boolean isInsert() {
        return self().builder.isInsert();
    }
    public boolean isDelete() {
        return self().builder.isDelete();
    }
    public boolean isUpdate() {
        return self().builder.isUpdate();
    }
    public boolean isWhere() {
        return self().builder.isWhere();
    }
    public  boolean isMultiTableQuery(){
        return self().builder.isMultiTableQuery();
    }
    public T whereToSelect() {
        this.builder.whereToSelect();
        return self();
    }
    public boolean isDynamicSql() {
        return self().builder.isDynamicSql();
    }
    public TaskType getTaskType(){
        return self().builder.getTaskType();
    }
    public  boolean isNotOnlyWhere(){
        return self().builder.isNotOnlyWhere();
    }
    protected  <E> String resolveColumn(PropertyGetter<E> getter) {

        return FieldResolver.fullField(getter);
    }

    //查询表-------------------------------------------------------------------------------------
    public T from(String table) {
        this.builder.from(table);
        return self();
    }

    public T from(Class<?> clazz) {
        return from(TableNameResolver.getTableName(clazz));
    }

    //子查询表
    public T from(BaseSQL subSql, String alias) {

        this.builder.from(jdbcModel.processSqlName(subSql) + alias + " ");
        return self();
    }


    public T where(String clause) {
        this.builder.where(clause);
        return self();
    }

    public T where(BaseSQL subSql) {
        if (subSql == null)
            return self();
        ensureTaskType(subSql, TaskType.WHERE);
        this.builder.where(jdbcModel.processSqlName(subSql));
        return self();
    }
    protected void ensureTaskType(BaseSQL sql, TaskType... expectedTaskTypes) {
        TaskType actualTaskType = sql.getTaskType();
        if (actualTaskType == null) {
            throw new UnsupportedOperationException("Unknown task type.");
        }

        // 如果实际任务类型是 DYNAMIC_SQL，则默认支持所有操作
        if (actualTaskType == TaskType.DYNAMIC_SQL) {
            return; // 默认通过校验
        }

        // 检查是否符合预期的任务类型
        if (!Arrays.asList(expectedTaskTypes).contains(actualTaskType)) {
            throw new UnsupportedOperationException(
                    String.format("Unsupported task type: %s. This method only supports %s tasks.",
                            actualTaskType, Arrays.toString(expectedTaskTypes))
            );
        }
    }
    // 分组条件-----
    public T having(String clause) {
        this.builder.having(clause);
        return self();
    }

    public T having(BaseSQL subSql) {

        this.builder.having(jdbcModel.processSqlName(subSql));
        return self();
    }

    //输出sql------------------------------------------
    public String toString() {

        String sql1 = this.builder.toString();

        jdbcModel.createJdbcSqlFromNameSql(sql1);
        return sql1;
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
    public String toOrderBy() {
        return this.builder.toOrderBy();
    }

    //参数设置--------------------------------------------

    public T setParameter(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlValue(subSql));
        return self();
    }

    public <E> T setParameter(PropertyGetter<E> getter, Object subSql) {
        return setParameter(FieldResolver.fullField(getter), subSql);
    }

    public T setParameter$(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlName(subSql));
        return self();
    }

    public <E> T setParameter$(PropertyGetter<E> getter, Object subSql) {
        return setParameter$(FieldResolver.fullField(getter), subSql);
    }
    //执行器-------------------------------------------------------------------------------------
    public Executor getExecutor() {
        if (executor == null)
            createExecutor();
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    protected Executor executor;
    //默认执行器为JdbcTemplateMapper
    public T createExecutor() {
        executor = DbTools.getExecutor();
        return self();
    }

    public T createExecutor(Connection connection) {

        executor = new DbUtilsExecutor(connection);
        return self();

    }

    public T createExecutor(DataSource dataSource) {
        executor = new DbUtilsExecutor(dataSource);
        return self();
    }

    public T createExecutor(Executor executor) {
        this.executor = executor;
        return self();
    }
    // 基础初始化
    protected void initComponents() {
        this.builder = new SqlModel();
        this.jdbcModel = new JdbcModel();
    }
}

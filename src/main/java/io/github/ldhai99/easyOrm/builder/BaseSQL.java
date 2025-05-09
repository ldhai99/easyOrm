package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;
import io.github.ldhai99.easyOrm.base.TaskType;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.model.JdbcModel;
import io.github.ldhai99.easyOrm.model.SqlModel;

import java.util.Arrays;
import java.util.Map;

public class BaseSQL <T extends BaseSQL<T>>{

    protected SqlModel builder = new SqlModel();
    protected JdbcModel jdbcModel = new JdbcModel();

    protected Executor executor;
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

    public T select(T subSql, String alias) {
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

    //查询表-------------------------------------------------------------------------------------
    public T from(String table) {
        this.builder.from(table);
        return self();
    }

    public T from(Class<?> clazz) {
        return from(TableNameResolver.getTableName(clazz));
    }

    //子查询表
    public T from(T subSql, String alias) {

        this.builder.from(jdbcModel.processSqlName(subSql) + alias + " ");
        return self();
    }


    public T where(String clause) {
        this.builder.where(clause);
        return self();
    }

    public T where(T subSql) {
        if (subSql == null)
            return self();
        ensureTaskType(subSql, TaskType.WHERE);
        this.builder.where(jdbcModel.processSqlName(subSql));
        return self();
    }
    protected void ensureTaskType(T sql, TaskType... expectedTaskTypes) {
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

    public T having(T subSql) {

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
}

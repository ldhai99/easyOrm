package io.github.ldhai99.easyOrm.model;

import io.github.ldhai99.easyOrm.base.TaskType;
import io.github.ldhai99.easyOrm.tools.SqlTools;
import io.github.ldhai99.easyOrm.constant.SqlKeywords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.ldhai99.easyOrm.constant.SqlKeywords.*;

public class SqlModel implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    // 增加，删除，修改表名
    private String updateTable;
    private String tableId = null;
    private TaskType taskType = TaskType.SELECT;

    private List<String> dynamicSqls = new ArrayList<>();
    private List<String> afterSelects = new ArrayList<>();
    // 字段
    private List<String> columns = new ArrayList<>();

    private List<String> tables = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> wheres = new ArrayList<>();

    private List<String> groupBys = new ArrayList<>();
    private List<String> havings = new ArrayList<>();
    private List<String> orderBys = new ArrayList<>();

    private List<String> lasts = new ArrayList<>();

    // 修改组
    private List<String> sets = new ArrayList<>();
    // 增加值
    private List<String> values = new ArrayList<>();

    // setValue 设置值名称: name
    private List<String> paraNames = new ArrayList<>();
    // setValue 分配的新名称: p000000
    private List<String> newParaNames = new ArrayList<>();

    public SqlModel() {
    }

    public SqlModel(String table) {
        this.tables.add(table);
    }

    protected SqlModel(SqlModel other) {
        this.taskType = other.taskType;
        this.updateTable = other.updateTable;

        this.dynamicSqls.addAll(other.dynamicSqls);
        this.afterSelects.addAll(other.afterSelects);
        this.columns.addAll(other.columns);
        this.tables.addAll(other.tables);
        this.joins.addAll(other.joins);
        this.wheres.addAll(other.wheres);
        this.groupBys.addAll(other.groupBys);
        this.havings.addAll(other.havings);
        this.orderBys.addAll(other.orderBys);
        this.lasts.addAll(other.lasts);

        this.sets.addAll(other.sets);
        this.values.addAll(other.values);
        this.paraNames.addAll(other.paraNames);
        this.newParaNames.addAll(other.newParaNames);
    }

    @Override
    public SqlModel clone() {
        return new SqlModel(this);
    }

    public boolean hasGroup() {
        return !groupBys.isEmpty();
    }

    public boolean hasCloumns() {
        return !columns.isEmpty();
    }

    public boolean hasTables() {
        return !tables.isEmpty();
    }

    // 指定任务 ------------------------------------------------------
    public SqlModel from(String table) {
        this.taskType = TaskType.SELECT;
        this.tables.add(table);
        return this;
    }

    public SqlModel update(String table) {
        this.updateTable = table;
        this.taskType = TaskType.UPDATE;
        return this;
    }

    public SqlModel delete(String table) {
        this.updateTable = table;
        this.taskType = TaskType.DELETE;
        return this;
    }

    public SqlModel insert(String table) {
        this.updateTable = table;
        this.taskType = TaskType.INSERT;
        return this;
    }

    public SqlModel where() {
        this.taskType = TaskType.WHERE;
        return this;
    }

    public SqlModel setDynamicSql(String dynamicSql) {
        this.taskType = TaskType.DYNAMIC_SQL;
        DynamicSql(dynamicSql);
        return this;
    }

    public SqlModel setTableId(String idField) {
        this.tableId = idField;
        return this;
    }

    // 初始化数据 ------------------------------------------------------------------------
    public SqlModel setColumn(String column) {
        this.columns.clear();
        this.columns.add(column);
        return this;
    }

    public SqlModel setWhere(String clause) {
        this.wheres.clear();
        this.where(clause);
        return this;
    }

    public SqlModel setHaving(String clause) {
        this.havings.clear();
        this.havings.add(clause);
        return this;
    }

    public SqlModel setGroup(String clause) {
        this.groupBys.clear();
        this.groupBys.add(clause);
        return this;
    }

    public SqlModel setJoins(String clause) {
        this.joins.clear();
        this.joins.add(clause);
        return this;
    }

    public SqlModel setLast(String clause) {
        this.lasts.clear();
        this.lasts.add(clause);
        return this;
    }

    // 增加数据 -----------------------------------------------------------------
    public SqlModel DynamicSql(String clause) {
        this.dynamicSqls.add(clause);
        return this;
    }

    public SqlModel afterSelect(String clause) {
        this.afterSelects.add(clause);
        return this;
    }

    // 增加更新部分
    public SqlModel set(String expr) {
        String[] parts1 = expr.split("=", 2);
        if (parts1.length < 2) {
            this.sets.add(expr);
            return this;
        }
        String targetName = parts1[0];
        String newValue = parts1[1];

        for (int i = 0; i < sets.size(); i++) {
            String[] parts = sets.get(i).split("=", 2);
            if (parts.length > 1 && parts[0].equals(targetName)) {
                sets.set(i, targetName + EQ + newValue);
                return this;
            }
        }
        this.sets.add(expr);
        return this;
    }

    public SqlModel value(String expr) {
        this.values.add(expr);
        return this;
    }

    public SqlModel insertColumn(String column, String nameParamPlaceholder) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).equals(column)) {
                columns.set(i, column);
                values.set(i, nameParamPlaceholder);
                return this;
            }
        }
        this.columns.add(column);
        this.value(nameParamPlaceholder);
        return this;
    }

    public SqlModel column(String column) {
        this.columns.add(column);
        return this;
    }

    public SqlModel last(String clause) {
        this.lasts.add(clause);
        return this;
    }

    public SqlModel column(String column, String alias) {
        this.columns.add(column + SPACE + AS + SPACE + alias);
        return this;
    }

    public SqlModel column(String column, boolean groupBy) {
        this.columns.add(column);
        if (groupBy) {
            this.groupBys.add(column);
        }
        return this;
    }

    public SqlModel distinct() {
        this.afterSelects.add(SPACE + DISTINCT + SPACE);
        return this;
    }

    public SqlModel groupBy(String expr) {
        this.groupBys.add(expr);
        return this;
    }

    public SqlModel join(String join) {
        this.joins.add(join);
        return this;
    }

    public SqlModel orderBy(String orderByString) {
        if (orderByString == null || orderByString.trim().isEmpty()) {
            throw new IllegalArgumentException("Order by string cannot be null or empty");
        }
        String[] orderByParts = orderByString.split(",");
        for (String part : orderByParts) {
            part = part.trim();
            if (!part.isEmpty()) {
                String upperPart = part.toUpperCase();
                if (upperPart.contains(" ASC")) {
                    String fieldName = part.replaceFirst("(?i)\\s+asc$", "").trim();
                    this.orderBy(fieldName, true);
                } else if (upperPart.contains(" DESC")) {
                    String fieldName = part.replaceFirst("(?i)\\s+desc$", "").trim();
                    this.orderBy(fieldName, false);
                } else {
                    this.orderBy(part, true);
                }
            }
        }
        return this;
    }

    public SqlModel orderBy(String clause, boolean ascending) {
        String direction = ascending ? ASC : DESC;
        this.orderBys.add(clause + SPACE + direction);
        return this;
    }

    public SqlModel where(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return this;
        } else {
            return whereAndHaving(expr, this.wheres);
        }
    }

    public SqlModel and(String expr) {
        return this.where(expr);
    }

    public SqlModel having(String expr) {
        return whereAndHaving(expr, this.havings);
    }

    private SqlModel whereAndHaving(String expr, List<String> clauses) {
        String lastClause = " ";
        if (!clauses.isEmpty()) {
            lastClause = SqlTools.rightTrim(clauses.get(clauses.size() - 1));
        }

        // 判断前一个条件是否以连接符或左括号结尾
        boolean prevEndsWithConnector = clauses.isEmpty() ||
                lastClause.endsWith(LPAREN) ||
                lastClause.endsWith(" or") ||
                lastClause.endsWith(" and");

        // 当前表达式是否以连接词开头
        String trimmedExpr = expr.trim();
        boolean currentStartsWithConnector =
                trimmedExpr.startsWith("or ") ||
                        trimmedExpr.startsWith("and ") ||
                        trimmedExpr.equals(RPAREN);

        if (prevEndsWithConnector) {
            if (!currentStartsWithConnector) {
                clauses.add(expr);
            }
            // 如果当前以连接词开头，且前一个是 ( / or / and，则直接添加（避免双重连接）
            else {
                clauses.add(expr);
            }
        } else {
            if (currentStartsWithConnector) {
                clauses.add(expr);
            } else {
                clauses.add(SPACE + AND + SPACE + expr);
            }
        }
        return this;
    }

    // 生成构建结果 ----------------------------------------------
    @Override
    public String toString() {
        switch (this.taskType) {
            case DYNAMIC_SQL:
                return this.toDynamicSql();
            case UPDATE:
                return this.toUpdate();
            case INSERT:
                return this.toInsert();
            case DELETE:
                return this.toDelete();
            case WHERE:
                return this.toWhere();
            default:
                return this.toSelect();
        }
    }

    public boolean isSelect() {
        return this.taskType == TaskType.SELECT && !this.tables.isEmpty();
    }

    public boolean isInsert() {
        return this.taskType == TaskType.INSERT;
    }

    public boolean isDelete() {
        return this.taskType == TaskType.DELETE;
    }

    public boolean isUpdate() {
        return this.taskType == TaskType.UPDATE;
    }

    public boolean isWhere() {
        return this.taskType == TaskType.WHERE;
    }

    public boolean isDynamicSql() {
        return this.taskType == TaskType.DYNAMIC_SQL;
    }

    public boolean isNotOnlyWhere() {
        return columns.isEmpty() && tables.isEmpty() && joins.isEmpty()
                && orderBys.isEmpty() && groupBys.isEmpty() && havings.isEmpty() && !wheres.isEmpty();
    }

    public SqlModel whereToSelect() {
        return this.setTaskType(TaskType.SELECT);
    }

    public boolean isMultiTableQuery() {
        if (taskType != TaskType.SELECT) {
            return false;
        }
        return tables.size() > 1 || !joins.isEmpty();
    }

    // 生成 SQL ------------------------------

    public String toSelect() {
        StringBuilder sql = new StringBuilder(SELECT).append(SPACE);

        appendList(sql, this.afterSelects, SPACE, SPACE);

        if (this.columns.isEmpty()) {
            sql.append(ASTERISK);
        } else {
            appendList(sql, this.columns, "", COMMA + SPACE);
        }

        appendList(sql, this.tables, SPACE + FROM + SPACE, COMMA + SPACE);
        appendList(sql, this.joins, SPACE, SPACE);
        appendList(sql, this.wheres, SPACE + WHERE + SPACE, SPACE);
        appendList(sql, this.groupBys, SPACE + GROUP_BY + SPACE, COMMA + SPACE);
        appendList(sql, this.havings, SPACE + HAVING + SPACE, SPACE + AND + SPACE);
        appendList(sql, this.orderBys, SPACE + ORDER_BY + SPACE, COMMA + SPACE);
        appendList(sql, this.lasts, SPACE, SPACE);

        return toNewPara(sql.toString());
    }

    public String toWhere() {
        StringBuilder sql = new StringBuilder();
        appendList(sql, this.wheres, SPACE, SPACE);
        return toNewPara(sql.toString());
    }

    public String toInsert() {
        StringBuilder sql = new StringBuilder(INSERT_INTO)
                .append(SPACE)
                .append(this.updateTable)
                .append(SPACE)
                .append(LPAREN);

        appendList(sql, this.columns, "", COMMA + SPACE);
        sql.append(RPAREN).append(SPACE).append(VALUES).append(SPACE).append(LPAREN);
        appendList(sql, this.values, "", COMMA + SPACE);
        sql.append(RPAREN);

        appendList(sql, this.lasts, SPACE, SPACE);
        return toNewPara(sql.toString());
    }

    public String toUpdate() {
        StringBuilder sql = new StringBuilder(UPDATE)
                .append(SPACE)
                .append(this.updateTable);

        appendList(sql, this.joins, SPACE, SPACE);
        appendList(sql, this.sets, SPACE + SET + SPACE, COMMA + SPACE);
        appendList(sql, this.wheres, SPACE + WHERE + SPACE, SPACE);
        appendList(sql, this.lasts, SPACE, SPACE);

        return toNewPara(sql.toString());
    }

    public String toDelete() {
        StringBuilder sql = new StringBuilder(DELETE_FROM)
                .append(SPACE)
                .append(this.updateTable);

        appendList(sql, this.wheres, SPACE + WHERE + SPACE, SPACE);
        appendList(sql, this.lasts, SPACE, SPACE);

        return toNewPara(sql.toString());
    }

    public String toDynamicSql() {
        StringBuilder sql = new StringBuilder();
        appendList(sql, this.dynamicSqls, SPACE, SPACE);
        return toNewPara(sql.toString());
    }

    public String toOrderBy() {
        StringBuilder sql = new StringBuilder();
        appendList(sql, this.orderBys, SPACE + ORDER_BY + SPACE, COMMA + SPACE);
        return toNewPara(sql.toString());
    }

    public SqlModel paraName(String name, String newName) {
        this.paraNames.add(name);
        this.newParaNames.add(newName);
        return this;
    }

    public String toNewPara(String sql) {
        String result = sql;
        for (int j = 0; j < paraNames.size(); j++) {
            result = result.replaceAll(":" + paraNames.get(j), newParaNames.get(j));
        }
        return result;
    }

    protected void appendList(StringBuilder sql, List<?> list, String init, String sep) {
        boolean first = true;
        for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
            Object item = it.next();
            if (first) {
                sql.append(init);
                first = false;
            } else {
                sql.append(sep);
            }
            sql.append(item);
        }
    }

    // 任务类型 ----------------------------------

    public TaskType getTaskType() {
        return taskType;
    }

    public SqlModel setTaskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }
}
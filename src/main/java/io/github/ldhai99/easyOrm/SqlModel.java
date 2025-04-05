package io.github.ldhai99.easyOrm;


import io.github.ldhai99.easyOrm.base.TaskType;
import io.github.ldhai99.easyOrm.tools.StringTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqlModel implements  Cloneable ,Serializable {
    private static final long serialVersionUID = 1L;

    //增加，删除，修改表名
    private String update_table;
    private String table_id=null;
    private TaskType taskType = TaskType.SELECT;


    private List<String> dynamicSqls = new ArrayList();
    private List<String> afterSelects = new ArrayList();
    //字段
    private List<String> columns = new ArrayList();

    private List<String> tables = new ArrayList();
    private List<String> joins = new ArrayList();
    private List<String> wheres = new ArrayList();

    private List<String> groupBys = new ArrayList();
    private List<String> havings = new ArrayList();
    private List<String> orderBys = new ArrayList();

    //修改组
    private List<String> sets = new ArrayList();
    //增加值
    private List<String> values = new ArrayList();

    //setValue设置值名称:name
    private List<String> paraNames = new ArrayList();
    //setValue分配的新名称:p000000
    private List<String> newParaNames = new ArrayList();
    public SqlModel() {
    }

    public SqlModel(String table) {
        this.tables.add(table);
    }


    protected SqlModel(SqlModel other)  {
        this.taskType=other.taskType;
        this.update_table=other.update_table;

        this.dynamicSqls.addAll(other.dynamicSqls);
        this.columns.addAll(other.columns);
        this.tables.addAll(other.tables);
        this.joins.addAll(other.joins);
        this.wheres.addAll(other.wheres);
        this.groupBys.addAll(other.groupBys);
        this.havings.addAll(other.havings);
        this.orderBys.addAll(other.orderBys);

        this.sets.addAll(other.sets);
        this.values.addAll(other.values);
        this.paraNames.addAll(other.paraNames);
        this.newParaNames.addAll(other.newParaNames);

    }
    public SqlModel clone() {
        return  new SqlModel(this);
    }
    public boolean hasGroup(){
        if(groupBys.size() == 0)
            return false;
        else
            return  true;
    }
    //指定任务------------------------------------------------------
    public SqlModel update(String table) {
        this.update_table = table;
        this.taskType = TaskType.UPDATE;
        return this;
    }

    public SqlModel delete(String table) {
        this.update_table = table;
        this.taskType = TaskType.DELETE;
        return this;
    }

    public SqlModel insert(String table) {
        this.update_table = table;
        this.taskType = TaskType.INSERT;
        return this;
    }

    public SqlModel where() {
        this.taskType = TaskType.WHERE;
        return this;
    }

    public SqlModel setDynamicSql(String DynamicSql) {
        this.taskType = TaskType.DYNAMIC_SQL;
        DynamicSql( DynamicSql);
        return this;
    }
    public SqlModel setTableId(String idFiled){
        this.table_id=idFiled;
        return  this;
    }
    //初始化数据------------------------------------------------------------------------
    public SqlModel setColumn(String name) {
        this.columns.clear();
        this.columns.add(name);
        return this;
    }
    public SqlModel setWhere(String name) {
        this.wheres.clear();
        this.where(name);
        return this;
    }
    public SqlModel setHaving(String name) {
        this.havings.clear();
        this.havings.add(name);
        return this;
    }

    public SqlModel setGroup(String name) {
        this.groupBys.clear();
        this.groupBys.add(name);
        return this;
    }
    public SqlModel setJoins(String name) {
        this.joins.clear();
        this.joins.add(name);
        return this;
    }


    //增加数据-----------------------------------------------------------------
    public SqlModel DynamicSql(String name) {
        this.dynamicSqls.add(name);
        return this;
    }
    public SqlModel afterSelect(String name) {
        this.afterSelects.add(name);
        return this;
    }
    //增加更新部分
    public SqlModel set(String expr) {

        //有了替换，没有添加
        String[] parts1 =expr.split("=", 2);
        // 要替换的name和新的value
        String targetName = parts1[0];
        String newValue = parts1[1];

        // 遍历列表并替换
        for (int i = 0; i < sets.size(); i++) {
            String[] parts = sets.get(i).split("=", 2); // 使用2作为限制来确保只分割一次
            if (parts.length > 1 && parts[0].equals(targetName)) {
                // 重新构造字符串并替换原元素
                sets.set(i, targetName + "=" + newValue);
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


    public SqlModel insertColumn(String name,String nameParamPlaceholder) {
        // 遍历列表并替换
        for (int i = 0; i < columns.size(); i++) {

            if (columns.get(i).equals(name)) {
                // 重新构造字符串并替换原元素
                columns.set(i, name);
                values.set(i,nameParamPlaceholder);
                return this;
            }
        }
        this.columns.add(name);
        this.value(nameParamPlaceholder);
        return this;
    }
    public SqlModel column(String name) {
        this.columns.add(name);
        return this;
    }
    public SqlModel column(String name,String alias) {
        this.columns.add(name+" as "+alias);
        return this;
    }

    public SqlModel column(String name, boolean groupBy) {
        this.columns.add(name);
        if (groupBy) {
            this.groupBys.add(name);
        }

        return this;
    }

    public SqlModel distinct() {
        this.afterSelects.add("distinct");
        return this;
    }


    public SqlModel from(String table) {
        this.tables.add(table);
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


    public SqlModel orderBy(String name) {
        this.orderBy(name, true);
        return this;
    }

    public SqlModel orderBy(String name, boolean ascending) {
        if (ascending) {
            this.orderBys.add(name + " asc");
        } else {
            this.orderBys.add(name + " desc");
        }

        return this;
    }


    public SqlModel where(String expr) {
        if(expr==null||expr.trim().length()==0)
            return  this;
        else
            return whereAndHaving(expr,this.wheres);
    }

    public SqlModel and(String expr) {

        return this.where(expr);
    }

    public SqlModel having(String expr) {

        return whereAndHaving(expr,this.havings);
    }

    private SqlModel whereAndHaving(String expr, List<String> wheres1) {

        String lastWhere = " ";
        if (wheres1.size() != 0) {
            lastWhere = StringTools.rightTrim(wheres1.get(wheres1.size() - 1));
        }

        String pre = "";
        //前面有连接（、 or、 and，就是最后部分是否有连接
        if ((wheres1.size() == 0) ||
                //判断字符串最后部分是不是连接，加空格前缀比较与避免or（doctor）和and后缀的字段名称，判断最好完全匹配
                (lastWhere.lastIndexOf("(") != -1 && lastWhere.lastIndexOf("(") == lastWhere.length() - 1) ||
                (lastWhere.lastIndexOf(" or") != -1 && lastWhere.lastIndexOf(" or") == lastWhere.length() - 3) ||
                (lastWhere.lastIndexOf(" and") != -1 && lastWhere.lastIndexOf(" and") == lastWhere.length() - 4)) {

            //自己有连接，重复，加空格后缀，避免or（org）和and开头的字段名称
            if (((expr.trim()+" ").indexOf("or ") == 0 ) || ((expr.trim()+" ").indexOf("and ") == 0)){
                //前面是开始（‘（’，'or','and'），不能再有开始符号，即(or、(and、 or or、 or and 等都是非法，但((是合法的。
            }
            //自己没有连接，直接加
            else{
                 wheres1.add(expr);
            }
        }
        //前面没有连接
        else {
            //自己有连接，直接加，）属于连接结束，加空格后缀，避免or（org）和and开头的字段名称
            if (((expr.trim()+" ").indexOf("or ") == 0 ) || ((expr.trim()+" ").indexOf("and ") == 0)
                    || expr.trim().equals(")")            ) {
                wheres1.add(expr);
            }
            //自己没有连接，添加默认连接and
            else {
                wheres1.add(" and " + expr);
            }

        }
        return this;
    }
    //生成构建结果----------------------------------------------
    public String toString() {

        if (this.getTaskType()==TaskType.DYNAMIC_SQL) {

            return this.toDynamicSql();
        } else if (this.getTaskType()==TaskType.UPDATE) {
            return this.toUpdate();
        } else if (this.getTaskType()==TaskType.INSERT) {
            return this.toInsert();
        } else if (this.getTaskType()==TaskType.DELETE) {
            return this.toDelete();
        } else if (this.getTaskType()==TaskType.WHERE) {
            return this.toWhere();
        } else
            return this.toSelect();
    }

    public boolean isSelect() {
        return this.getTaskType()==TaskType.SELECT? true:false;
    }

    public boolean isInsert() {
        return this.getTaskType()==TaskType.INSERT? true:false;
    }
    public boolean isDelete() {
        return this.getTaskType()==TaskType.DELETE? true:false;
    }
    public boolean isUpdate() {
        return this.getTaskType()==TaskType.UPDATE? true:false;
    }
    public boolean isWhere() {
        return this.getTaskType()==TaskType.WHERE? true:false;
    }
    public boolean isDynamicSql() {
        return this.getTaskType()==TaskType.DYNAMIC_SQL? true:false;
    }
    public boolean isNotOnlyWhere(){
        if(columns.isEmpty()&&tables.isEmpty()&&joins.isEmpty()
                &&orderBys.isEmpty()&&groupBys.isEmpty()&&havings.isEmpty() && !wheres.isEmpty())
            return true;
        return false;
    }
    public SqlModel whereToSelect(){
        return this.setTaskType(TaskType.SELECT);
    }
    //生成sql------------------------------

    public String toSelect() {
        StringBuilder sql = new StringBuilder("select ");

        this.appendList(sql, this.afterSelects, " ", " ");

        if (this.columns.size() == 0) {
            sql.append("*");
        } else {
            this.appendList(sql, this.columns, "", ", ");
        }

        this.appendList(sql, this.tables, " from ", ", ");
        this.appendList(sql, this.joins, " ", " ");
        //自带and 或者 or
        this.appendList(sql, this.wheres, " where ", " ");
        this.appendList(sql, this.groupBys, " group by ", ", ");
        this.appendList(sql, this.havings, " having ", " and ");

        this.appendList(sql, this.orderBys, " order by ", ", ");

        return toNewPara(sql.toString());
    }


    public String toWhere() {
        StringBuilder sql = new StringBuilder(" ");
        this.appendList(sql, this.wheres, "  ", " ");
        return toNewPara(sql.toString());
    }

    public String toInsert() {
        StringBuilder sql = (new StringBuilder("insert into ")).append(this.update_table).append(" (");
        this.appendList(sql, this.columns, "", ", ");
        sql.append(") values (");
        this.appendList(sql, this.values, "", ", ");
        sql.append(")");
        return toNewPara(sql.toString());
    }

    public String toUpdate() {
        StringBuilder sql = (new StringBuilder("update ")).append(this.update_table);
        this.appendList(sql, this.joins, " ", " ");

        this.appendList(sql, this.sets, " set ", ", ");
        //自带and 或者 or
        this.appendList(sql, this.wheres, " where ", " ");
        return toNewPara(sql.toString());
    }

    public String toDelete() {
        StringBuilder sql = (new StringBuilder(" delete from ")).append(this.update_table);

        //自带and 或者 or
        this.appendList(sql, this.wheres, " where ", " ");
        return toNewPara(sql.toString());
    }

    public String toDynamicSql() {
        StringBuilder sql = new StringBuilder();
        this.appendList(sql, this.dynamicSqls, "  ", " ");
        return toNewPara(sql.toString());
    }

    public SqlModel paraName(String name, String newName) {
        this.paraNames.add(name);
        this.newParaNames.add(newName);
        return this;
    }
    public String toNewPara(String sql) {
        int i = paraNames.size();
        String ret = sql;

        for (int j = 0; j < i; j++) {
            ret = ret.replaceAll(":" + paraNames.get(j), newParaNames.get(j));
        }
        return ret;
    }
    protected void appendList(StringBuilder sql, List<?> list, String init, String sep) {
        boolean first = true;

        for (Iterator i$ = list.iterator(); i$.hasNext(); first = false) {
            Object s = i$.next();
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }

            sql.append(s);
        }

    }
    //任务类型----------------------------------

    public TaskType getTaskType() {
        return taskType;
    }
    public SqlModel setTaskType( TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

}
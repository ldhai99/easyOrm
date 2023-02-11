package com.github.ldhai99.easyOrm;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcModel implements  Serializable {
    private static final long serialVersionUID = 1L;


    //构建过程--存储的参数值
    private Map<String, Object> parameterMap = new HashMap();

    //构建完成--传过来的sql
    private String sql=null;

    //jdbc执行前--需要的SQL和参数数组
    private String jdbcSql=null;
    private List<Object> paramsList=new ArrayList<>();

    //分配参数名
    private String prefixParaName="";
    private int paramIndex;

    private static final String NAME_REGEX = "[a-z][_a-z0-9]*";
    private static final String PARAM_REGEX = ":([a-z][_a-z0-9]*)";
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z][_a-z0-9]*", 2);
    private static final Pattern PARAM_PATTERN = Pattern.compile(":([a-z][_a-z0-9]*)", 2);
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\?", 2);


    public JdbcModel() {
        prefixParaName="p"+Integer.toHexString(System.identityHashCode(this));
    }

    //分配参数名称
    private String allocateParametername() {

        return prefixParaName + this.paramIndex++;
    }


    //设置字段---值
    public JdbcModel addParameter(String name, Object value) {

        this.parameterMap.put(name, value);
        return this;
    }

    //属性值get,set
    public String getSql() {

        return this.sql;
    }

    public JdbcModel setSql(String sql) {
        this.sql = sql;
        return this;
    }
    public Map<String, Object> getParameterMap() {

        return this.parameterMap;
    }

    public void createJdbcSqlFromNameSql(String sql1){
        this.setSql(sql1);

        SqlAndParams sqlAndParams=createSqlAndParams();
        this.setJdbcSql(sqlAndParams.getSql());
        this.setParamsList(sqlAndParams.getParams());

    }


    //把占位符？替换为自动分配的:名称
    public String createSqlNameParams( String sqlPara, Object ... valuesPara){

        Matcher m = PLACEHOLDER_PATTERN.matcher(sqlPara);
        StringBuilder psSql = new StringBuilder();
        int indexPlace = 0;
        int index=-1;

        while(m.find(indexPlace)) {

            psSql.append(sqlPara.substring(indexPlace, m.start()));

            indexPlace = m.end();
            String param = this.allocateParametername();

            psSql.append(" :" + param);

            index=index+1;
            this.parameterMap.put(param,valuesPara[index]);
        }

        psSql.append(sqlPara.substring(indexPlace));
        return psSql.toString();

    }
    //把:名字参数，替换为？，并形成参数数组[]。
    SqlAndParams createSqlAndParams() {
        StringBuilder psSql = new StringBuilder();
        List<Object> paramValues = new ArrayList();
        Matcher m = PARAM_PATTERN.matcher(this.sql);
        int indexPlace = 0;

        while(m.find(indexPlace)) {
            psSql.append(this.sql.substring(indexPlace, m.start()));
            String name = m.group(1);
            indexPlace = m.end();
            if (!this.parameterMap.containsKey(name)) {
                throw new IllegalArgumentException("Unknown parameter '" + name + "' at position " + m.start());
            }

            psSql.append("?");
            paramValues.add(this.parameterMap.get(name));
        }

        psSql.append(this.sql.substring(indexPlace));
        return new SqlAndParams(psSql.toString(), paramValues);
    }

    static class SqlAndParams {
        private String sql;
        private List<Object> paramsList;

        private SqlAndParams(String sql, List<Object> params) {
            this.sql = sql;
            this.paramsList = params;
        }

        public List<Object> getParams() {
            return this.paramsList;
        }

        public String getSql() {
            return this.sql;
        }
    }
    public String processSqlName(Object value) {
        if (value instanceof SQL) {
            SQL sql = (SQL) value;
            //合并参数值
            mergeParameterMap(sql);
            //占位-替换
            return " (" + sql.toString() + ") ";
        }else if (value instanceof List){
            return  processSqlNameList(" (",",",") ",(List<?>) value);
        }
        else if (value instanceof Number){

            return  value.toString();
        }
        else {

            return  (String) value;
        }
    }
    public String processSqlValue(Object value) {
        //处理Sql类型
        if (value instanceof SQL) {
            SQL sql = (SQL) value;
            //合并参数值
            mergeParameterMap(sql);
            //占位-替换
            return "(" + sql.toString() + ")";
        }
        //处理list类型
        else if (value instanceof List){
            return  processSqlValueList("(",",",")",(List<?>) value);
        }
        else {
            //获取参数名
            String param = this.allocateParametername();
            //保存参数值
            this.addParameter(param, value);

            return  " :" + param;
        }
    }
    private String processSqlNameList( String open,String separator,String close,List<?> values) {
        StringBuilder sb = new StringBuilder();

        sb.append(open);
        boolean first = true;

        for (Iterator i$ = values.iterator(); i$.hasNext(); first = false) {
            Object value = i$.next();

            if (!first) {
                sb.append(separator);
            }
            sb.append(value);
        }
        sb.append(close);
        return sb.toString();
    }
    private String processSqlValueList( String open,String separator,String close,List<?> values) {
        StringBuilder sb = new StringBuilder();

        sb.append(open);
        boolean first = true;

        for (Iterator i$ = values.iterator(); i$.hasNext(); first = false) {
            Object value = i$.next();

            if (!first) {
                sb.append(separator);
            }
            String param = this.allocateParametername();
            this.addParameter(param, value);
            sb.append(":").append(param);
        }
        sb.append(close);
        return sb.toString();
    }
    public void mergeParameterMap(SQL subSQL) {
        //合并参数Map
        // Map<String, Object> map = new HashMap<>(this.getParameterMap());

        subSQL.getJdbcDataModel().getParameterMap().forEach((key, value) -> this.getParameterMap().merge(key, value, (v1, v2) -> v1));

    }
    public String getJdbcSql() {
        return jdbcSql;
    }

    public void setJdbcSql(String jdbcSql) {
        this.jdbcSql = jdbcSql;
    }

    public List<Object> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<Object> paramsList) {
        this.paramsList = paramsList;
    }

}

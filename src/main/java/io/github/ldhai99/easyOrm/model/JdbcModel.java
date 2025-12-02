package io.github.ldhai99.easyOrm.model;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.builder.BaseSQL;
import io.github.ldhai99.easyOrm.context.DbType;
import io.github.ldhai99.easyOrm.dbenum.DbEnum;
import io.github.ldhai99.easyOrm.dialect.Dialect;
import io.github.ldhai99.easyOrm.dialect.LikeType;
import io.github.ldhai99.easyOrm.tools.SqlTools;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcModel implements Serializable {
    private static final long serialVersionUID = 1L;


    //构建过程--存储的参数值
    private Map<String, Object> parameterMap = new HashMap();

    //构建完成--传过来的sql
    private String sql = null;

    //jdbc执行前--需要的SQL和参数数组
    private String jdbcSql = null;
    private List<Object> paramsList = new ArrayList<>();

    //分配参数名
    private String prefixParaName = "";
    private int paramIndex;

    private static final String NAME_REGEX = "[a-z][_a-z0-9]*";
    private static final String PARAM_REGEX = ":([a-z][_a-z0-9]*)";
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-z][_a-z0-9]*", 2);
    private static final Pattern PARAM_PATTERN = Pattern.compile(":([a-z][_a-z0-9]*)", 2);
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\?", 2);
    // 方言助手（仅一行代码）
    private final DialectHelper dialectHelper = new DialectHelper();

    public JdbcModel() {
        prefixParaName = "p" + Integer.toHexString(System.identityHashCode(this));
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

    public void createJdbcSqlFromNameSql(String sql1) {
        this.setSql(sql1);

        SqlAndParams sqlAndParams = createSqlAndParams();
        this.setJdbcSql(sqlAndParams.getSql());
        this.setParamsList(sqlAndParams.getParams());

    }


    //把占位符？替换为自动分配的:名称
    public String createSqlNameParams(String sqlPara, Object... valuesPara) {

        Matcher m = PLACEHOLDER_PATTERN.matcher(sqlPara);
        StringBuilder psSql = new StringBuilder();
        int indexPlace = 0;
        int index = -1;

        while (m.find(indexPlace)) {

            psSql.append(sqlPara.substring(indexPlace, m.start()));

            indexPlace = m.end();
            String param = this.allocateParametername();

            psSql.append(" :" + param);

            index = index + 1;
            this.parameterMap.put(param, valuesPara[index]);
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

        while (m.find(indexPlace)) {
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
    // ============ 方言相关方法（极简代理） ============

    public void setDbType(DbType dbType) {
        dialectHelper.setDbType(dbType);
    }

    public void setDialect(Dialect dialect) {
        dialectHelper.setDialect(dialect);
    }

    public Dialect getDialect() {
        return dialectHelper.getDialect();
    }

    public String processSqlName(Object value) {
        if (value instanceof BaseSQL) {
            BaseSQL sql = (BaseSQL) value;
            //合并参数值
            mergeParameterMap(sql);

            String sqls = sql.toString();
            if (SqlTools.isEmpty(sqls))
                return "";
            else
                //占位-替换
                return " (" + sqls + ") ";

        } else if (value instanceof List) {
            return processSqlNameList(" (", ",", ") ", (List<?>) value);
        } else if (value instanceof Number) {

            return value.toString();
        } else {

            // 关键：使用方言助手包装标识符
            return dialectHelper.wrapIdentifier(value.toString());
        }
    }

    /**
     * 应用分页
     */
    public String applyPagination(String sql, int offset, int limit) {
        return dialectHelper.applyPagination(sql, offset, limit);
    }



    public String processSqlValue(Object value) {
        //处理Sql类型
        if (value instanceof BaseSQL) {
            BaseSQL sql = (BaseSQL) value;
            //合并参数值
            mergeParameterMap(sql);

            String sqls = sql.toString();
            if (SqlTools.isEmpty(sqls))
                return "";
            else
                //占位-替换
                return " (" + sqls + ") ";

        }
        // 2. 处理列表类型
        else if (value instanceof List) {
            return processSqlValueList("(", ",", ")", (List<?>) value);
        }
        // 3. 新增：处理枚举类型
        else if (value instanceof DbEnum) {
            return processDbEnumValue((DbEnum) value);
        }
        // 4. 新增：处理枚举类型
        else if (value instanceof Enum) {
            return processEnumValue((Enum) value);
        } else {
            return appendValue(value);
        }
    }
    /**
     * 处理LIKE值
     */
    public String processLikeValue(String value, LikeType likeType) {
        return dialectHelper.processLikeValue(value, likeType);
    }
    private String appendValue(Object value) {
        //获取参数名
        String param = this.allocateParametername();
        //保存参数值
        this.addParameter(param, value);

        return " :" + param;
    }

    /**
     * 专门处理枚举值的转换逻辑
     */
    private String processDbEnumValue(DbEnum enumValue) {
        // 3.1 检查是否是 DbEnum 接口的实现（推荐方式）
        if (enumValue instanceof DbEnum) {
            Object dbValue = ((DbEnum) enumValue).getValue();
            return processSqlValue(dbValue); // 递归处理实际值
        }

        // 3.2 检查是否有 getValue() 方法（兼容模式）
        try {
            Method getValueMethod = enumValue.getClass().getMethod("getValue");
            if (getValueMethod != null && getValueMethod.getReturnType() != void.class) {
                Object dbValue = getValueMethod.invoke(enumValue);
                return processSqlValue(dbValue); // 递归处理
            }
        } catch (Exception e) {
            // 方法不存在或调用失败，继续后续处理
        }

        // 3.3 默认处理：使用枚举名称
        return appendValue(enumValue.name());

    }

    /**
     * 专门处理枚举值的转换逻辑
     */
    private String processEnumValue(Enum enumValue) {

        // 1 检查是否有 getValue() 方法（兼容模式）
        try {
            Method getValueMethod = enumValue.getClass().getMethod("getValue");
            if (getValueMethod != null && getValueMethod.getReturnType() != void.class) {
                Object dbValue = getValueMethod.invoke(enumValue);
                return processSqlValue(dbValue); // 递归处理
            }
        } catch (Exception e) {
            // 方法不存在或调用失败，继续后续处理
        }

        // 2 默认处理：使用枚举名称
        return appendValue(enumValue.name());
    }

    private String processSqlNameList(String open, String separator, String close, List<?> values) {
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

    private String processSqlValueList(String open, String separator, String close, List<?> values) {
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

    public void mergeParameterMap(BaseSQL subSQL) {
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

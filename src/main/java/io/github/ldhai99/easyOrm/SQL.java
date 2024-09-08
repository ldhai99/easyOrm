package io.github.ldhai99.easyOrm;



import io.github.ldhai99.easyOrm.executor.DbUtilsMapper;
import io.github.ldhai99.easyOrm.executor.IMapper;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

public class SQL {
    private static final long serialVersionUID = 1L;

    protected SqlModel builder = new SqlModel();
    protected JdbcModel jdbcModel = new JdbcModel();

    private IMapper executor;

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
    public SQL(IMapper executor) {
        createExecutor(executor);
    }

    //传入执行器
    public SQL(NamedParameterJdbcTemplate template) {
        executor = new JdbcTemplateMapper(template);
    }


    //克隆
    protected SQL(SQL sql) {
        this.builder=sql.builder.clone();
        this.jdbcModel.mergeParameterMap(sql);
        this.executor= sql.executor;
    }
    public SQL clone(){
        return new SQL(this);
    }

    //默认执行器为JdbcTemplateMapper
    public SQL createExecutor(){
        executor =  JdbcTemplateMapper.getMapper();
        return  this;
    }
    public SQL createExecutor(Connection connection){

        executor = new DbUtilsMapper(connection);
        return  this;

    }
    public SQL createExecutor(DataSource dataSource){
        executor = new DbUtilsMapper(dataSource);
        return  this;
    }
    public SQL createExecutor(IMapper executor){
        this.executor = executor;
        return  this;
    }

    //大学，构造，并传入执行器
    public static SQL MAPPER(Connection connection) {
        return new SQL( connection);
    }
    public static SQL MAPPER(DataSource dataSource) {
        return new SQL( dataSource);
    }

    public  static SQL MAPPER(IMapper executor) {
        return new SQL( executor);

    }

    //小写，设置执行器
    public  SQL mapper(Connection connection) {
        createExecutor(connection);
        return this;
    }
    public  SQL mapper(DataSource dataSource) {
        createExecutor(dataSource);
        return this;
    }

    public  SQL mapper(IMapper executor) {
        createExecutor(executor);

        return this;
    }

    //静态工厂方法-----------------------------------------------------------------------------------
    public static SQL SELECT(String table) {
        return new SQL().select(table);
    }
    public static SQL SELECT(SQL subSql,String alias) {
        return new SQL().select(subSql,alias);
    }

    public static SQL UPDATE(String table) {
        return new SQL().update(table);
    }

    public static SQL DELETE(String table) {
        return new SQL().delete(table);
    }

    public static SQL INSERT(String table) {
        return new SQL().insert(table);
    }
    public static SQL WHERE() {    return new SQL().where();    }
    public static SQL WHERE(String name) {    return new SQL().where().where(name);    }
    public static SQL WHERE(SQL sql) {    return new SQL().where().where(sql);    }
    public static SQL SETSQL(String directSql, Object... values) {
        return new SQL().setSql(directSql, values);
    }





    //开始任务-----------------------------------------------------------
    public SQL select(String table) {
        this.builder.from(table);
        return this;
    }
    public SQL select(SQL subSql, String alias) {
        return  this.from(subSql,alias);
    }

    public SQL update(String table) {
        this.builder.update(table);
        return this;
    }

    public SQL delete(String table) {
        this.builder.delete(table);
        return this;
    }

    public SQL insert(String table) {
        this.builder.insert(table);
        return this;
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

        this.builder.where(jdbcModel.processSqlName(subSql));
        return this;
    }
    public SQL having(String name) {
        this.builder.having(name);
        return this;
    }
    public SQL having(SQL subSql) {

        this.builder.having(jdbcModel.processSqlName(subSql));
        return this;
    }

    //直接给出sql和参数名称，后面用set设置值配合使用--------------------------------------------------
    public SQL setSql(String directSql, Object... values) {

        this.builder.setDirectSql(jdbcModel.createSqlNameParams(directSql, values));
        return this;
    }
    public SQL setSql(SQL subSql) {

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
    public    Object[] getParamsList() {
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


    public SQL groupBy(String name) {
        this.builder.groupBy(name);
        return this;
    }


    //
    public SQL join(String join) {
        this.builder.join(join);
        return this;
    }
    public SQL join(String table, String on) {
        this.join("inner join", table, on);
        return this;
    }
    public SQL leftJoin(String table, String on) {
        this.join("left join", table, on);
        return this;
    }
    public SQL rightJoin(String table, String on) {
        this.join("right join", table, on);
        return this;
    }
    public SQL fullJoin(String table, String on) {
        this.join("full join", table, on);
        return this;
    }
    public SQL join(String join, String table, String on) {
        if (on != null && on.trim().length() != 0) {
            this.builder.join(join + " " + table + " on " + on);
        } else
            this.builder.join(join + " " + table);
        return this;
    }


    public SQL orderBy(String name) {
        this.orderBy(name, true);
        return this;
    }

    public SQL orderBy(String name, boolean ascending) {
        this.builder.orderBy(name, ascending);
        return this;
    }


    public SqlModel getBuilder() {
        return this.builder;
    }

    /* public PreparedStatementCreator count(final Dialect dialect) {
          return new PreparedStatementCreator() {
              public PreparedStatement createPreparedStatement(Connection con)  {
                  return SelectBuilder.this.getPreparedStatementCreator().setSql(dialect.createCountSelect(SelectBuilder.this.builder.toString())).createPreparedStatement(con);
              }
          };
      }*/
/*
    public PreparedStatementCreator page(final Dialect dialect, final int limit, final int offset) {
        return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con)  {
                return SelectBuilder.this.getPreparedStatementCreator().setSql(dialect.createPageSelect(SelectBuilder.this.builder.toString(), limit, offset)).createPreparedStatement(con);
            }
        };
    }
*/
    //代理SqlDataModel---------------------------
//聚合函数列----------------------------------------
    public SQL sum(String name) {
        return aggregate("sum", name);
    }

    public SQL sum(String name, String alias) {
        return aggregate("sum", name, alias);
    }

    public SQL avg(String name) {
        return aggregate("avg", name);
    }

    public SQL avg(String name, String alias) {
        return aggregate("avg", name, alias);
    }

    public SQL max(String name) {
        return aggregate("max", name);
    }

    public SQL max(String name, String alias) {
        return aggregate("max", name, alias);
    }


    public SQL min(String name) {
        return aggregate("min", name);
    }

    public SQL min(String name, String alias) {
        return aggregate("min", name, alias);
    }

    public SQL count() {
        this.builder.column("count(" + "*" + ") ");
        return this;
    }

    public SQL count(String alias) {
        this.builder.column("count(" + "*" + ") " + alias + " ");
        return this;
    }

    public SQL aggregate(String function, String name) {
        return aggregate(function, name, null);
    }

    public SQL aggregate(String function, String name, String alias) {
        String alias1 = alias;
        if (alias1 == null)
            alias1 = name;
        this.builder.column(" " + function + "(" + name + ") " + alias1 + " ");

        return this;
    }

    //查询列----------------------------------------
    public SQL column(String name) {
        this.builder.column(name);
        return this;
    }

    public SQL column(String name, boolean groupBy) {
        this.builder.column(name, groupBy);
        return this;
    }

    //子查询列----------------------------------------------------
    public SQL column(SQL subSql, String alias) {

        this.builder.column(jdbcModel.processSqlName(subSql)+ alias + " ");
        return this;
    }
    //0、通用查询-----------age=18----------------------------------------------------
    //name+operator+value
    private SQL nameOperatorValue(Object name, String operator, Object value) {

        this.where(String.format(" %s %s %s ", jdbcModel.processSqlName(name),operator, jdbcModel.processSqlValue(value)));
        return this;
    }

    //operator+value------exists（value）-------
    public SQL operatorValue(String operator, SQL subSQL) {
        this.where(String.format("  %s %s ",operator, jdbcModel.processSqlValue(subSQL)));
        return this;
    }
    //一、比较谓词----------------------------------------------------
    public SQL func(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    //一、比较谓词----------------------------------------------------
    public SQL where(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }


    public SQL eq(Object name, Object value) {
        return nameOperatorValue(name, "=", value);
    }

    public SQL neq(Object name, Object value) {
        return nameOperatorValue(name, "<>", value);
    }

    public SQL gt(Object name, Object value) {
        return nameOperatorValue(name, ">", value);
    }

    public SQL gte(Object name, Object value) {
        return nameOperatorValue(name, ">=", value);
    }

    public SQL lt(Object name, Object value) {
        return nameOperatorValue(name, "<", value);
    }

    public SQL lte(Object name, Object value) {
        return nameOperatorValue(name, "<=", value);
    }


    //二、LIKE 谓词——字符串的部分一致查询
    public SQL like(Object name, Object value) {
        return likeOperator(name, "like", value);
    }

    public SQL like_(Object name, Object value) {
        return likeOperator(name, "like_", value);
    }

    public SQL notLike(Object name, Object value) {
        return likeOperator(name, "notLike", value);
    }

    public SQL likeLeft(Object name, Object value) {
        return likeOperator(name, "likeLeft", value);
    }

    public SQL likeRight(Object name, Object value) {
        return likeOperator(name, "likeRight", value);
    }

    public SQL likeOperator(Object name, String operator, Object value) {

        Object newvalue=value;

        //对值预处理
        if (!(value instanceof SQL)) {
            String oldValue = (String) value;

            if (operator.equalsIgnoreCase("like"))
                newvalue = "%" + oldValue + "%";
            else if (operator.equalsIgnoreCase("notlike")) {
                newvalue = "%" + oldValue + "%";
            } else if (operator.equalsIgnoreCase("likeLeft")) {
                newvalue = "%" + oldValue;
            } else if (operator.equalsIgnoreCase("likeRight")) {
                newvalue = oldValue + "%";
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
        this.between(name,  "  between " ,value1,value2);

        return this;
    }

    public SQL notBetween(Object name, Object value1, Object value2) {

        this.between(name,  "  not between " ,value1,value2);

        return this;
    }

    public SQL between(Object name,String between, Object value1, Object value2) {
        String placehoder1 = jdbcModel.processSqlValue(value1);
        String placehoder2 = jdbcModel.processSqlValue(value2);
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + between +placehoder1 + " and " +  placehoder2);

        return this;
    }

    //四、IS NULL、IS NOT NULL——判断是否为 NULL

    public SQL isNull(Object name) {

        this.isNull(name,"is  null");
        return this;
    }

    public SQL isNotNull(Object name) {

        this.isNull(name,"is not null");
        return this;
    }

    public SQL isNull(Object name,String isNull) {
        String namePlacehoder = jdbcModel.processSqlName(name);
        this.where(namePlacehoder + " "+ isNull);
        return this;
    }
    //五、IN 谓词——OR 的简便用法
    public SQL or() {

        this.where(" or ");
        return this;
    }

    public SQL in(Object name, List<?> values) {
        return nameOperatorValue(name, "in", values);

    }

    public SQL in(Object name, Object... values) {

        return nameOperatorValue(name, "in", new ArrayList<>(Arrays.asList(values)));
    }
    public SQL in(Object name, SQL subSql) {

        return nameOperatorValue(name, "in", subSql);
    }
    public SQL notIn(Object name, List<?> values) {
        return nameOperatorValue(name, "not in", values);
    }
    public SQL notIn(Object name, SQL subSql) {

        return nameOperatorValue(name, "not in", subSql);
    }
    public SQL notIn(Object name, Object... values) {
        return nameOperatorValue(name, "not in", new ArrayList<>(Arrays.asList(values)));

    }



    //七、EXIST 谓词
    //
    public SQL exists(SQL subSQL) {
        return operatorValue( "exists", subSQL);
    }

    public SQL notExists(SQL subSQL) {
        return operatorValue( "not exists", subSQL);
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



    //翻页---------------------------


    //更新设置--------------------------------------------


    //直接遍历字段，然后设置
    public SQL setMap(String fields, Map value) {
        String[] parts = fields.split(",\\s*");
        for (int i = 0; i < parts.length; i++) {
            if(value!=null )
                set(parts[i], new DbParameter(parts[i], value.get(parts[i])));
            else
                set(parts[i], new DbParameter(parts[i], null));
        }
        return this;
    }
    //设置字段---值
    public SQL set(String name, Object value) {

        set(name, new DbParameter(name, value));
        return this;
    }

    public SQL set(String name, Object value, String datatype) {
        set(name, new DbParameter(name, value, datatype));
        return this;
    }

    public SQL set(String name, Object value, String datatype, boolean allowNull) {
        set(name, new DbParameter(name, value, datatype, allowNull));
        return this;
    }

    public void set(String name, DbParameter pmt) {
        set1(name,pmt.getValue());

    }

    public SQL set1(String name, Object subSql) {
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


            this.builder.insertColumn(name,nameParamPlaceholder);

        }

    }

    //参数设置--------------------------------------------

    public SQL setValue(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlValue(subSql));
        return this;
    }


    public SQL setValue$(String name, Object subSql) {

        //设置占位符号
        this.builder.paraName(name, jdbcModel.processSqlName(subSql));
        return this;
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
                .setSql(" :arg0  :arg1" )
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
                SQL.SETSQL(" :arg0 " + union + " :arg1")
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
    public int update()  {
        return executor.update(this);
    }
    public int update(DataSource ds)  {
        createExecutor(ds);
        return update();
    }
    public int update(Connection conn)  {
        createExecutor(conn);
        return update();
    }
    public int update(IMapper mapper)  {
        createExecutor(mapper);
        return update();
    }
    //增加，返回主键
    public Number insert()  {
        return executor.insert(this);
    }

    public Number insert(DataSource ds)  {
        createExecutor(ds);
        return insert();
    }
    public Number insert(Connection conn)  {
        createExecutor(conn);
        return insert();
    }
    public Number insert(IMapper mapper)  {
        createExecutor(mapper);
        return insert();
    }
    //执行存储过程
    public int execute()  {

        return executor.execute(this);
    }

    public int execute(DataSource ds)  {
        createExecutor(ds);
        return execute();
    }
    public int execute(Connection conn)  {
        createExecutor(conn);
        return execute();
    }

    public int execute(IMapper mapper)  {
        createExecutor(mapper);
        return execute();
    }
    //查询数据库-----------------------------------------------------------------------------------

    //返回单列单行数据
    public String getString()  {
        return executor.getString(this);
    }
    public String getString(DataSource ds)  {
        createExecutor(ds);
        return getString();
    }
    public String getString(Connection conn)  {
        createExecutor(conn);
        return getString();
    }
    public String getString(IMapper mapper)  {
        createExecutor(mapper);
        return getString();
    }

    public Integer getInteger()  {
        return executor.getInteger(this);
    }
    public Integer getInteger(DataSource ds)  {
        createExecutor(ds);
        return getInteger();
    }
    public Integer getInteger(Connection conn)  {
        createExecutor(conn);
        return getInteger();
    }

    public Long getLong()  {
        return executor.getLong(this);
    }
    public Long getLong(DataSource ds)  {
        createExecutor(ds);
        return getLong();
    }
    public Long getLong(Connection conn)  {
        createExecutor(conn);
        return getLong();
    }

    public Float getFloat()  {
        return executor.getFloat(this);
    }
    public Float getFloat(DataSource ds)  {
        createExecutor(ds);
        return getFloat();
    }
    public Float getFloat(Connection conn)  {
        createExecutor(conn);
        return getFloat();
    }

    public Double getDouble()  {

        return executor.getDouble(this);
    }
    public Double getDouble(DataSource ds)  {
        createExecutor(ds);
        return getDouble();
    }
    public Double getDouble(Connection conn)  {
        createExecutor(conn);
        return getDouble();
    }

    public Number getNumber()  {
        return executor.getNumber(this);
    }
    public Number getNumber(Connection conn)  {
        createExecutor(conn);
        return getNumber();
    }
    public Number getNumber(DataSource ds)  {
        createExecutor(ds);
        return getNumber();
    }

    public BigDecimal getBigDecimal()  {

        return executor.getBigDecimal(this);
    }

    public BigDecimal getBigDecimal(DataSource ds)  {
        createExecutor(ds);
        return getBigDecimal();
    }
    public BigDecimal getBigDecimal(Connection conn)  {
        createExecutor(conn);
        return getBigDecimal();
    }

    public Date getDate()  {

        return executor.getDate(this);
    }
    public Date getDate(DataSource ds)  {
        createExecutor(ds);
        return getDate();
    }
    public Date getDate(Connection conn)  {
        createExecutor(conn);
        return getDate();
    }

    public <T> T getValue(Class<T> requiredType)  {
        return executor.getValue(this,requiredType);
    }
    public <T> T getValue(DataSource ds,Class<T> requiredType)  {
        createExecutor(ds);
        return getValue(requiredType);
    }
    public <T> T getValue(Connection conn,Class<T> requiredType)  {
        createExecutor(conn);
        return getValue(requiredType);
    }

    //返回单列list数据
    public List<String> getStrings()  {

        return executor.getStrings(this);
    }
    public List<String> getStrings(DataSource ds)  {
        createExecutor(ds);
        return getStrings();
    }
    public List<String> getStrings(Connection conn)  {
        createExecutor(conn);
        return getStrings();
    }

    public List<Integer> getIntegers()  {
        return executor.getIntegers(this);
    }
    public List<Integer> getIntegers(DataSource ds)  {
        createExecutor(ds);
        return getIntegers();
    }
    public List<Integer> getIntegers(Connection conn)  {
        createExecutor(conn);
        return getIntegers();
    }

    public List<Long> getLongs()  {

        return executor.getLongs(this);
    }
    public List<Long> getLongs(DataSource ds)  {
        createExecutor(ds);
        return getLongs();
    }
    public List<Long> getLongs(Connection conn)  {
        createExecutor(conn);
        return getLongs();
    }


    public List<Double> getDoubles()  {
        return executor.getDoubles(this);
    }
    public List<Double> getDoubles(DataSource ds)  {
        createExecutor(ds);
        return getDoubles();
    }
    public List<Double> getDoubles(Connection conn)  {
        createExecutor(conn);
        return getDoubles();
    }

    public List<Float> getFloats()  {
        return executor.getFloats(this);
    }
    public List<Float> getFloats(DataSource ds)  {
        createExecutor(ds);
        return getFloats();
    }
    public List<Float> getFloats(Connection conn)  {
        createExecutor(conn);
        return getFloats();
    }

    public List<Number> getNumbers()  {

        return executor.getNumbers(this);
    }

    public List<Number> getNumbers(DataSource ds)  {
        createExecutor(ds);
        return getNumbers();
    }
    public List<Number> getNumbers(Connection conn)  {
        createExecutor(conn);
        return getNumbers();
    }

    public List<BigDecimal> getBigDecimals()  {

        return executor.getBigDecimals(this);
    }
    public List<BigDecimal> getBigDecimals(DataSource ds)  {
        createExecutor(ds);
        return getBigDecimals();
    }
    public List<BigDecimal> getBigDecimals(Connection conn)  {
        createExecutor(conn);
        return getBigDecimals();
    }


    public List<Date> getDates()  {

        return executor.getDates(this);
    }
    public List<Date> getDates(DataSource ds)  {
        createExecutor(ds);
        return getDates();
    }
    public List<Date> getDates(Connection conn)  {
        createExecutor(conn);
        return getDates();
    }


    public <T> List<T> getValues(Class<T> requiredType)  {

        return executor.getValues(this,requiredType);
    }
    public <T> List<T> getValues(DataSource ds,Class<T> requiredType)  {
        createExecutor(ds);
        return getValues(requiredType);
    }
    public <T> List<T> getValues(Connection conn,Class<T> requiredType)  {
        createExecutor(conn);
        return getValues(requiredType);
    }


    //返回单行数据

    public Map<String, Object> getMap()  {

        return executor.getMap(this);
    }
    public Map<String, Object> getMap(DataSource ds)  {
        createExecutor(ds);
        return getMap();
    }
    public Map<String, Object> getMap(Connection conn)  {
        createExecutor(conn);
        return getMap();
    }

    //返回多行数据
    public List<Map<String, Object>> getMaps()  {

        return executor.getMaps(this);
    }
    public List<Map<String, Object>> getMaps(DataSource ds)  {
        createExecutor(ds);
        return getMaps();
    }
    public List<Map<String, Object>> getMaps(Connection conn)  {
        createExecutor(conn);
        return getMaps();
    }


    //返回Bean实体
    public <T> T getBean(Class<T> T)  {

        return executor.getBean(this,T);
    }
    public <T> T getBean(DataSource ds,Class<T> T)  {
        createExecutor(ds);
        return getBean(T);
    }
    public <T> T getBean(Connection conn,Class<T> T)  {
        createExecutor(conn);
        return getBean(T);
    }

    //返回Bean list
    public <T> List<T> getBeans(Class<T> T)  {

        return executor.getBeans(this,T);
    }
    public <T> List<T> getBeans(DataSource ds,Class<T> T)  {
        createExecutor(ds);
        return getBeans(T);
    }
    public <T> List<T> getBeans(Connection conn,Class<T> T)  {
        createExecutor(conn);
        return getBeans(T);
    }

    //查询应用-------------判断是否存在------------------------------------------------
    public boolean isExists()  {
       return this.executor.isExists(this);
    }
    public boolean isExists(DataSource ds)  {
        createExecutor(ds);
        return isExists();
    }
    public boolean isExists(Connection conn)  {
        createExecutor(conn);
        return isExists();
    }
    //对一个查询给出记录数
    public Number getCount()  {
        return this.executor.getCount(this);
    }
    public Number getCount(DataSource ds)  {
        createExecutor(ds);
        return getCount();
    }
    public Number getCount(Connection conn)  {
        createExecutor(conn);
        return getCount();
    }

}
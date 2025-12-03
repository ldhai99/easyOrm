package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.builder.ExecutorHandler;
import io.github.ldhai99.easyOrm.context.DbType;
import io.github.ldhai99.easyOrm.dialect.Dialect;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Executor {
    /**
     * 获取数据库类型
     */
    public DbType getDbType();


    /**
     * 设置数据库类型
     */
    public void setDbType(DbType dbType);
    /**
     * 获取方言
     */
    public Dialect getDialect();

    public void setDialect(Dialect dialect);


    /**
     * 获取数据源（可选，用于重新检测等场景）
     */
    default DataSource getDataSource() {
        return null;
    }
    //代理-------------------------------------------------执行类
    //执行增加，删除，修改，返回记录个数
    public int update(ExecutorHandler sql)   ;
    public int delete(ExecutorHandler sql)   ;
    //插入，返回主键
    public Number insert(ExecutorHandler sql)   ;

    //执行存储过程及ddl
    public int execute(ExecutorHandler sql)   ;

    //查询数据库---------------------------------------------------------

    //返回单列单行数据
    //返回字符串
    public String getString (ExecutorHandler sql)   ;

    //返回数字型
    public Number getNumber(ExecutorHandler sql)   ;
    public Integer getInteger(ExecutorHandler sql)   ;
    public Long getLong(ExecutorHandler sql)   ;
    public Float getFloat(ExecutorHandler sql)   ;
    public Double getDouble(ExecutorHandler sql)   ;
    public  BigDecimal getBigDecimal(ExecutorHandler sql) ;
    //返回日期型
    public Date getDate (ExecutorHandler sql) ;
    public < T > T getValue (ExecutorHandler sql, Class<T> requiredType)   ;


    //返回单列list数据
    public List<String> getStrings (ExecutorHandler sql)   ;
    public  List<Number> getNumbers (ExecutorHandler sql) ;
    public List<Integer> getIntegers(ExecutorHandler sql)   ;
    public List<Long> getLongs(ExecutorHandler sql)   ;
    public List<Float> getFloats(ExecutorHandler sql)   ;
    public List<Double> getDoubles(ExecutorHandler sql)   ;

    public  List<BigDecimal> getBigDecimals (ExecutorHandler sql) ;
    public  List<Date> getDates (ExecutorHandler sql)   ;
    public < T > List<T> getValues (ExecutorHandler sql, Class<T> requiredType)   ;

    //返回单行数据

    public Map<String, Object> getMap(ExecutorHandler sql)  ;
    //返回多行数据
    public List<Map<String, Object>> getMaps(ExecutorHandler sql)   ;
    //返回Bean实体
    public <T> T getBean(ExecutorHandler sql, Class<T> T)  ;

    //返回Bean list
    public <T> List<T> getBeans(ExecutorHandler sql, Class<T> T)  ;



}

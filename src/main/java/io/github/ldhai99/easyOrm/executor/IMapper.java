package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.SQL;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IMapper {

    //代理-------------------------------------------------执行类
    //执行增加，删除，修改，返回记录个数
    public int update(SQL sql)   ;
    public int delete(SQL sql)   ;
    //插入，返回主键
    public Number insert(SQL sql)   ;

    //执行存储过程及ddl
    public int execute(SQL sql)   ;

    //查询数据库---------------------------------------------------------

    //返回单列单行数据
    //返回字符串
    public String getString (SQL sql)   ;

    //返回数字型
    public Number getNumber(SQL sql)   ;
    public Integer getInteger(SQL sql)   ;
    public Long getLong(SQL sql)   ;
    public Float getFloat(SQL sql)   ;
    public Double getDouble(SQL sql)   ;
    public  BigDecimal getBigDecimal(SQL sql) ;
    //返回日期型
    public Date getDate (SQL sql) ;
    public < T > T getValue ( SQL sql,Class<T> requiredType)   ;


    //返回单列list数据
    public List<String> getStrings (SQL sql)   ;
    public  List<Number> getNumbers (SQL sql) ;
    public List<Integer> getIntegers(SQL sql)   ;
    public List<Long> getLongs(SQL sql)   ;
    public List<Float> getFloats(SQL sql)   ;
    public List<Double> getDoubles(SQL sql)   ;

    public  List<BigDecimal> getBigDecimals (SQL sql) ;
    public  List<Date> getDates (SQL sql)   ;
    public < T > List<T> getValues (SQL sql, Class<T> requiredType)   ;

    //返回单行数据

    public Map<String, Object> getMap(SQL sql)  ;
    //返回多行数据
    public List<Map<String, Object>> getMaps(SQL sql)   ;
    //返回Bean实体
    public <T> T getBean(SQL sql,Class<T> T)  ;

    //返回Bean list
    public <T> List<T> getBeans(SQL sql,Class<T> T)  ;


    public boolean isExists(SQL sql)  ;


    public Number getCount(SQL sql);

}

package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.JdbcModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IExecutor {


    public JdbcModel getJdbcDataModel() ;

    public IExecutor setJdbcDataModel(JdbcModel jdbcModel) ;

    //代理-------------------------------------------------执行类
    //执行增加，删除，修改，返回记录个数
    public int update()   ;

    //插入，返回主键
    public Number insert()   ;

    //执行存储过程及ddl
    public int execute()   ;

    //查询数据库---------------------------------------------------------

    //返回单列单行数据
    //返回字符串
    public String string ()   ;

    //返回数字型
    public Number number()   ;
    public  BigDecimal bigDecimal() ;
    //返回日期型
    public Date date () ;
    public < T > T value ( Class<T> requiredType)   ;


    //返回单列list数据
    public List<String> strings ()   ;
    public  List<Number> numbers () ;
    public  List<BigDecimal> bigDecimals () ;
    public  List<Date> dates ()   ;
    public < T > List<T> values ( Class<T> requiredType)   ;
    //返回单行数据

    public Map<String, Object> map()  ;
    //返回多行数据
    public List<Map<String, Object>> maps()   ;
    //返回Bean实体
    public <T> T bean(Class<T> T)  ;

    //返回Bean list
    public <T> List<T> beans(Class<T> T)  ;

}

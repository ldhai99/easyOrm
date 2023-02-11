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
    public int update()  throws SQLException ;

    //插入，返回主键
    public Number insert()  throws SQLException ;

    //执行存储过程及ddl
    public int execute()  throws SQLException ;

    //查询数据库---------------------------------------------------------

    //返回单列单行数据
    //返回字符串
    public String string ()  throws SQLException ;

    //返回数字型
    public Number number()  throws SQLException ;
    public  BigDecimal bigDecimal() throws SQLException;
    //返回日期型
    public Date date () throws SQLException;
    public < T > T value ( Class<T> requiredType)  throws SQLException ;


    //返回单列list数据
    public List<String> strings ()  throws SQLException ;
    public  List<Number> numbers () throws SQLException;
    public  List<BigDecimal> bigDecimals () throws SQLException;
    public  List<Date> dates ()  throws SQLException ;
    public < T > List<T> values ( Class<T> requiredType)  throws SQLException ;
    //返回单行数据

    public Map<String, Object> map()  throws SQLException ;
    //返回多行数据
    public List<Map<String, Object>> maps()  throws SQLException ;
    //返回Bean实体
    public <T> T bean(Class<T> T)  throws SQLException;

    //返回Bean list
    public <T> List<T> beans(Class<T> T)  throws SQLException;

}

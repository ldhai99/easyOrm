package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.JdbcModel;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DbUtilsExecutor implements IExecutor {
    private Connection connection;
    private DataSource dataSource;
    private JdbcModel jdbcModel;
    QueryRunner qr=new QueryRunner();

    DbUtilsExecutor(){

    }
    public DbUtilsExecutor(Connection connection){
        this.connection=connection;
   }
   public DbUtilsExecutor(DataSource dataSource){
        this.dataSource=dataSource;
    }

    //执行增删改

    @Override
    public JdbcModel getJdbcDataModel() {
        return jdbcModel;
    }

    @Override
    public IExecutor setJdbcDataModel(JdbcModel jdbcModel) {
        this.jdbcModel = jdbcModel;
        return this;
    }
    public Connection  getConnection(){
        if(this.connection!=null)
            return  this.connection;
        // 从事务管理器获取当前事务的连接
        this.connection= DataSourceUtils.getConnection(dataSource);
        return  this.connection;
    }
    //执行增加，删除，修改，返回记录个数
    public int update()  {
        try {
            getConnection();
            return qr.update(connection, jdbcModel.getJdbcSql(), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //执行存储过程及ddl
    public int execute()  {
        try {
            getConnection();
            return qr.update(connection, jdbcModel.getJdbcSql(), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //插入，返回主键
    public Number insert()  {
        try {
            getConnection();
            return qr.insert(connection, jdbcModel.getJdbcSql(),new ScalarHandler<Number>(), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //查询数据库---------------------------------------------------------

    //返回单列单行数据
    public String string ()  {
        return  value(String.class);
    }

    public  Number number ()  {
        return  value(Number.class);
    }
    public  BigDecimal bigDecimal ()  {
        return  value(BigDecimal.class);
    }
    public Date date ()  {
        return  value(Date.class);
    }

    public < T > T value ( Class<T> requiredType)  {

        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new ScalarHandler<T>(), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //返回单列list数据
    public List<String> strings ()  {
        return  values(String.class);
    }
    public  List<Number> numbers()  {
        return  values(Number.class);
    }
    public  List<BigDecimal> bigDecimals ()  {
        return  values(BigDecimal.class);
    }
    public  List<Date> dates ()  {
        return  values(Date.class);
    }
    public < T > List<T> values ( Class<T> requiredType) {
        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new ColumnListHandler<T>(), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //返回单行数据map

    public Map<String, Object> map()  {
        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new MapHandler(new BasicRowProcessor(new GenerousBeanProcessor())), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //返回多行数据maps
    public List<Map<String, Object>> maps()  {

        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new MapListHandler(new BasicRowProcessor(new GenerousBeanProcessor())), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //返回Bean实体
    public <T> T bean(Class<T> c) {
        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new BeanHandler<T>(c,new BasicRowProcessor(new GenerousBeanProcessor())), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //返回Bean list
    public <T> List<T> beans(Class<T> c) {
        try {
            getConnection();
            return qr.query(connection, jdbcModel.getJdbcSql(), new BeanListHandler<T>(c,new BasicRowProcessor(new GenerousBeanProcessor())), jdbcModel.getParamsList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

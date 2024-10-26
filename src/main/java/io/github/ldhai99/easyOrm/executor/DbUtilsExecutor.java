package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public class DbUtilsExecutor extends AbstractExecutor {
    private Connection connection;
    private DataSource dataSource;

    QueryRunner qr;

    public DbUtilsExecutor(){
        if(DbTools.getDataSource()!=null){
            // 从事务管理器获取当前事务的连接，全局数据源
            this.connection= DataSourceUtils.getConnection(DbTools.getDataSource());
            qr=new QueryRunner();
        }
    }
    public DbUtilsExecutor(Connection connection){
        this.connection=connection;
        qr=new QueryRunner();
   }
   public DbUtilsExecutor(DataSource dataSource){
        qr=new QueryRunner();
        this.dataSource=dataSource;
       // 从事务管理器获取当前事务的连接,传入数据源
       this.connection= DataSourceUtils.getConnection(dataSource);
    }

    //执行增删改


    ///QueryRunner,占位符和jdbc一样，参数为对象数组
    //执行增加，删除，修改，返回记录个数
    public int update(SQL sql)  {
        try {
            return qr.update(connection, sql.getJdbcSql(), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(SQL sql) {
        try {
            return qr.update(connection, sql.getJdbcSql(), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //执行存储过程及ddl
    public int execute(SQL sql)  {
        try {
            return qr.update(connection,  sql.getJdbcSql(), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //插入，返回主键
    public Number insert(SQL sql)  {
        try {
            return qr.insert(connection, sql.getJdbcSql(),new ScalarHandler<Number>(), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //查询数据库---------------------------------------------------------

    //返回单列单行数据

    public < T > T getValue ( SQL sql,Class<T> requiredType)  {

        try {

            return qr.query(connection, sql.getJdbcSql(), new ScalarHandler<T>(), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //返回单列list数据

    public < T > List<T> getValues (SQL sql, Class<T> requiredType) {
        try {
           //System.out.println(sql);
            return qr.query(connection, sql.getJdbcSql(), new ColumnListHandler<T>(), sql.getParamsList());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //返回单行数据map

    public Map<String, Object> getMap(SQL sql)  {
        try {
            return qr.query(connection, sql.getJdbcSql(), new MapHandler(new BasicRowProcessor(new GenerousBeanProcessor())), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //返回多行数据maps
    public List<Map<String, Object>> getMaps(SQL sql)  {

        try {
            return qr.query(connection, sql.getJdbcSql(), new MapListHandler(new BasicRowProcessor(new GenerousBeanProcessor())), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //返回Bean实体
    public <T> T getBean(SQL sql,Class<T> c) {
        try {
            return qr.query(connection, sql.getJdbcSql(), new BeanHandler<T>(c,new BasicRowProcessor(new GenerousBeanProcessor())), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //返回Bean list
    public <T> List<T> getBeans(SQL sql,Class<T> c) {
        try {
            return qr.query(connection, sql.getJdbcSql(), new BeanListHandler<T>(c,new BasicRowProcessor(new GenerousBeanProcessor())), sql.getParamsList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

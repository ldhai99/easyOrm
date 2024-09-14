package io.github.ldhai99.easyOrm.tools;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbTools {

    private static DataSource dataSource;
    private static NamedParameterJdbcTemplate template;
    static {
        Properties pro = new Properties();
        InputStream in = DbTools.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            pro.load(in);
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
    public static Connection getConnection(DataSource dataSource) {
       return DataSourceUtils.getConnection(dataSource);
    }
    public static void close(Connection conn,DataSource dataSource){
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    public static DataSource getDataSource(){
        return dataSource;
    }
    //获取jdbcTemplate
    public static NamedParameterJdbcTemplate getTemplate(){
        if(template==null)
            template=new  NamedParameterJdbcTemplate(dataSource);
        return template;
    }
    //执行器
    public static Executor getExecutor(){
        return  new JdbcTemplateExecutor(getTemplate());
    }

    public static void close(Connection conn, Statement stat, ResultSet res){
        if (res != null){
            try {
                res.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (stat != null){
            try {
                stat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println(getConnection());
    }



}

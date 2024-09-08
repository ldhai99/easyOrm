package io.github.ldhai99.easyOrm.tools;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import io.github.ldhai99.easyOrm.executor.IMapper;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public static DataSource getDataSource(){
        return dataSource;
    }
    public static NamedParameterJdbcTemplate getTemplate(){
        if(template==null)
            template=new  NamedParameterJdbcTemplate(dataSource);
        return template;
    }
    public static IMapper getMapper(){
        return  new JdbcTemplateMapper(getTemplate());
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

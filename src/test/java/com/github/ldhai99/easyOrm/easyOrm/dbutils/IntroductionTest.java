package com.github.ldhai99.easyOrm.easyOrm.dbutils;

import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.executor.DbUtilsExecutor;
import com.github.ldhai99.easyOrm.tools.DruidUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class IntroductionTest {

    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DruidUtils.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }
    @Test
    public  void chainType() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void factory() throws SQLException {
        //静态工厂方法
        System.out.println(
                SQL.GET(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void executor() throws SQLException {
        //传入执行器
        System.out.println(
                SQL.GET(new DbUtilsExecutor(con)).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void getint() throws SQLException {
        //获取李四的年龄
        System.out.println(
                SQL.GET(con).select("student").column("age").eq("name","李四").getInteger()
        );

    }
    @Test
    public  void notOrder() throws SQLException {

        System.out.println(
                new SQL(con).eq("age", 18).column("name,age").select("student").getMaps()
        );
    }
    @Test
    public  void manytimes() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name").column("age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void preventInjection() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18)
        );
    }
}

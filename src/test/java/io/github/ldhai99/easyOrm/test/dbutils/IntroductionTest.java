package io.github.ldhai99.easyOrm.test.dbutils;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.DbUtilsExecutor;

import io.github.ldhai99.easyOrm.tools.DbTools;
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
        con = DbTools.getConnection();
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
                SQL.ofExecutor(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void executor() throws SQLException {
        //传入执行器
        System.out.println(
                SQL.ofExecutor(new DbUtilsExecutor(con)).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void getint() throws SQLException {
        //获取李四的年龄
        System.out.println(
                SQL.ofExecutor(con).select("student").column("age").eq("name","李四").getInteger()
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

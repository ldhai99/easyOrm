package com.github.ldhai99.easyOrm.easyOrm.dbutils;


import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.tools.DruidUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class SelectTest {
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
    public  void coloum() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").column("sex").eq("age",18).getMaps()
        );

    }

    @Test
    public  void coloumaggregate() throws SQLException {

        System.out.println(
                new SQL(con).select("student")
                        .count("count")//计数
                        .sum("age","sum")//合计
                        .min("age","min")//最小
                        .max("age","max")//最大
                        .avg("age","avg")//平均
                        .aggregate("avg","age","avg1")//传递聚合函数avg
                        .getMaps()
        );
    }
    @Test
    public  void order() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("age,name")
                        .orderBy("age")
                        .getMaps()
        );
    }
    @Test
    public  void group() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("sex")
                        .count("count")//计数
                        .min("age","min")//最小
                        .max("age","max")//最大
                        .groupBy("sex")
                        .getMaps()
        );
    }
    @Test
    public  void having() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("sex")
                        .count("count")//计数
                        .min("age","min")//最小
                        .max("age","max")//最大
                        .avg("age","avg")//平均
                        .groupBy("sex")
                        .having("avg(age)>=20")
                        .getMaps()
        );
    }

    @Test
    public  void join() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("student.age,student1.name")
                        .join("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void leftjoin() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("student.age,student1.name")
                        .leftJoin("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void rightJoin() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("student.age,student1.name")
                        .rightJoin("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void crossJoin() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("student.age,student1.name")
                        .join("cross join","student1", null)
                        .getMaps()
        );
    }
    //union------------------------------------
    @Test
    public void union() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("name","张三")
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .unionAll(
                                SQL.SETSQL("select name,age from student "))
                        .orderBy("age",false)

                        .getMaps()
        );
    }
}
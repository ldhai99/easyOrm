package io.github.ldhai99.easyOrm.test.dbutils;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;

import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class QueryTest {


    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DataSourceManager.getConnection();
        ds = DataSourceManager.getDataSource();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    //获取单个值------------------------------------
    @Test
    public void integer() throws SQLException {

        System.out.println(
                new SQL(ds).select("student").column("10.3333").eq("age", 18).getInteger()
        );
    }

    @Test
    public void longType() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getLong()
        );
    }

    @Test
    public void floatType() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getFloat()
        );
    }

    @Test
    public void doubleType() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getDouble()
        );
    }

    @Test
    public void bigDecimal() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getBigDecimal()
        );
    }

    @Test
    public void string() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name").eq("age", 18).getString()
        );
    }

    @Test
    public void date() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("create_time").eq("age", 18).getDate()
        );
    }

    @Test
    public void value() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("age").eq("age", 18).getValue(Short.class)
        );
    }

    //获取多列单个值------------------------------------
    @Test
    public void integers() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getIntegers()
        );
    }

    @Test
    public void longs() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getLongs()
        );
    }

    @Test
    public void floats() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getFloats()
        );
    }

    @Test
    public void doubles() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getDoubles()
        );
    }

    @Test
    public void bigDecimals() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("10.3333").eq("age", 18).getBigDecimals()
        );
    }

    @Test
    public void strings() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name").eq("age", 18).getStrings()
        );
    }

    @Test
    public void dates() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("create_time").eq("age", 18).getDates()
        );
    }

    @Test
    public void values() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("age").eq("age", 18).getValues(Short.class)
        );
    }

    //获取Map类型-------------------------------------
    @Test
    public void map() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("*").eq("age", 18).getMap()
        );
    }

    @Test
    public void maps() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }

    //获取bean类型-------------------------------------
    @Test
    public void bean() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("*").eq("age", 18).getBean(Student.class)
        );
    }

    @Test
    public void beans() throws SQLException {

        System.out.println(
                new SQL(con).select("student").eq("age", 18).getBeans(Student.class)
        );
    }

    //应用扩展-------------------------------------------------
    //---------判断是否存在-----------
    @Test
    public void isExists() throws SQLException {

        System.out.println(
                new SQL(con).select("student").eq("age", 18).isExists()
        );
        System.out.println(
                new SQL(con).select("student").eq("age", 118).isExists()
        );
    }
    //---------返回记录个数-----------
    @Test
    public void getCount() throws SQLException {

        new SQL(con).update("student").set("age",18).eq("name", "李四").update();
        System.out.println(
                new SQL(con).select("student").eq("age", 18).getCount()
        );
    }
    @Test
    public void whereTest() throws SQLException {

        System.out.println(
                new SQL(con).column("*")
                        .column(new SQL().column("age").from("student").eq("name", "李四"), "age1")

                        .from("student a")
                        .eq("age", 18).or()
                        .begin().gte("age", 18).lte("age", 88).end()
                        .begin()
                        .in("name", new ArrayList<>(Arrays.asList("张三", "李四", "王五"))).end()
                        .in("age", new SQL().column("age").from("student")
                                .between("age", 1, 100))
                        .between("age", 2, 99)
                        .exists(new SQL().column("age").from("student b ").where(" a.id=b.id ")
                                .between("b.age", 3, 98)).getMaps()
        );

    }



}

package io.github.ldhai99.easyOrm.test.dbutils;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class WhereTest {
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
    //等于------------------------------------
    @Test
    public void where() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").where("age=18").getMaps()
        );
    }
    //等于------------------------------------
    @Test
    public void eq() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public void eqString() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("name", "李四").getMaps()
        );
    }
    //不等于------------------------------------
    @Test
    public void neq() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").neq("age", 18).getMaps()
        );
    }
    //大于------------------------------------
    @Test
    public void gt() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").gt("age", 18).getMaps()
        );
    }
    //大于等于------------------------------------
    @Test
    public void gte() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").gte("age", 18).getMaps()
        );
    }
    //小于------------------------------------
    @Test
    public void lt() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").lt("age", 18).getMaps()
        );
    }
    //小于等于------------------------------------
    @Test
    public void lte() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").lte("age", 18).getMaps()
        );
    }

    //范围查询------------------------------------
    @Test
    public void between() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").between("age", 18,20).getMaps()
        );
    }
    //范围以外------------------------------------
    @Test
    public void notbetween() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").notBetween("age", 18,20).getMaps()
        );
    }

    //是否为空------------------------------------
    @Test
    public void isNull() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age",18).isNull("name").getMaps()
        );
    }
    @Test
    public void isNotNull() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age",18).isNotNull("name").getMaps()
        );
    }
    //是or in------------------------------------
    @Test
    public void or() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age",18).or().eq("age",19).or().eq("age",17).getMaps()
        );
    }
    @Test
    public void in() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .in("age",17,18,19).getMaps()
        );
    }
    @Test
    public void in2() throws SQLException {

        Object[] objects={17,18,19};
        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .in("age",objects).getMaps()
        );
    }

    @Test
    public void in3() throws SQLException {

        List  objects=new ArrayList<>();
        objects.add(17);
        objects.add(18);
        objects.add(19);
        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .in("age",objects).getMaps()
        );
    }

    @Test
    public void notin() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .notIn("age",17,18,19).getMaps()
        );
    }


    //exists------------------------------------
    @Test
    public void exists() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .exists(SQL.SELECT("student1").where("student.name=student1.name"))
                        .getMaps()
        );
    }
    //notexists------------------------------------
    @Test
    public void notexists() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .notExists(SQL.SELECT("student1").where("student.name=student1.name"))
                        .getMaps()
        );
    }

    //beginend------------------------------------
    @Test
    public void beginend() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age,sex")
                        .begin().begin().eq("age",17).or().eq("age",18).end()
                        .eq("sex","女").end()
                        .getMaps()
        );
    }
    //last------------------------------------
    @Test
    public void last() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").orderBy("age").last(" limit 0,2").getMaps()
        );
    }

}

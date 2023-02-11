package com.github.ldhai99.easyOrm.easyOrm.dbutils;


import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.tools.DruidUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WhereOnlyTest {
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
    public void wherebuilder() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("sex")
                        .count("count")//计数
                        .min("age", "min")//最小
                        .max("age", "max")//最大
                        .avg("age", "avg")//平均
                        .groupBy("sex")
                        .having(SQL.WHERE().gte("avg(age)",18))
                        .getMaps()
        );
    }
    //beginend------------------------------------
    @Test
    public void beginend() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age,sex")
                        .where(SQL.WHERE(SQL.WHERE().eq("age",17).or().eq("age",18))
                                .eq("sex","女")).eq("name","李七")
                        .getMaps()
        );
    }

    @Test
    public void funcTest() throws SQLException {
        System.out.println(
                new SQL(con).select("student").column("name,create_time")
                        .where(this.eqMonth1("create_time","2022-12-01")).getMaps()
        );
    }
    //常用函数
    public SQL eqMonth(String name,String value){
        return  SQL.WHERE().eq("TIMESTAMPDIFF(MONTH,:name,:value)",0)
                .setValue$("name",name)
                .setValue("value",value) ;
    }
    public SQL eqMonth1(String name,String value){
        return  SQL.SETSQL("TIMESTAMPDIFF(MONTH,:name,:value)=0")
                .setValue$("name",name)
                .setValue("value",value) ;
    }

}
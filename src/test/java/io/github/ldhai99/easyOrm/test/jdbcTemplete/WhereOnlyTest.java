package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhereOnlyTest {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }
    @Test
    public void wherebuilder() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("sex")
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
                new SQL(executor).select("student").column("name,age,sex")
                        .where(SQL.WHERE(SQL.WHERE().eq("age",17).or().eq("age",18))
                                .eq("sex","女")).eq("name","李七")
                        .getMaps()
        );
    }

    @Test
    public void funcTest() throws SQLException {
        System.out.println(
                new SQL(executor).select("student").column("name,create_time")
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

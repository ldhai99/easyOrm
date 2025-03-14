package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectTest {
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
    public  void coloum() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age").column("sex").eq("age",18).getMaps()
        );

    }

    @Test
    public  void coloumaggregate() throws SQLException {

        System.out.println(
                new SQL(executor).select("student")
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
                new SQL(executor).select("student").column("age,name")
                        .orderBy("age")
                        .getMaps()
        );
    }
    @Test
    public  void group() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("sex")
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
                new SQL(executor).select("student").column("sex")
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
                new SQL(executor).select("student").column("student.age,student1.name")
                        .join("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void joinAlis() throws SQLException {

        System.out.println(
                new SQL(executor).select("student a").column("a.age,b.name")
                        .join("student1 b", "b.name=a.name")
                        .toString()
        );
    }
    @Test
    public  void leftjoin() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("student.age,student1.name")
                        .leftJoin("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void rightJoin() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("student.age,student1.name")
                        .rightJoin("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void crossJoin() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("student.age,student1.name")
                        .join("cross join","student1", null)
                        .getMaps()
        );
    }
    //union------------------------------------
    @Test
    public void union() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age").eq("name","张三")
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

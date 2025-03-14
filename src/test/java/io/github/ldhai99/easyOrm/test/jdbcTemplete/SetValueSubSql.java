package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetValueSubSql {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }

    //setSql
    @Test
    public void setSql() throws SQLException {

        System.out.println(
                new SQL(executor).setSql("select name,age from student where age = ?", 18).getMaps()
        );
    }

    //setSql+setValue
    @Test
    public void setSqlSetValue() throws SQLException {

        System.out.println(
                new SQL(executor)
                        .setSql("select name,age from student where age = :arg1")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(executor)
                        .setSql("select name,age from student where age = :arg1")
                        .setValue("arg1", 18).getMaps()
        );
    }
    @Test
    public void setSqlSetValue$() throws SQLException {

        System.out.println(
                new SQL(executor)
                        .setSql("select name,age from student where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(executor)
                        .setSql("select name,age from student where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
                        .getMaps()
        );
    }

    @Test
    public void setSqlSetSql() throws SQLException {

        System.out.println(
                new SQL(executor)
                        .setSql("select :field").setValue$("field","name,age")
                        .setSql(" from :table").setValue$("table","student")
                        .setSql(" where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(executor)
                        .setSql("select :field").setValue$("field","name,age")
                        .setSql(" from :table").setValue$("table","student")
                        .setSql(" where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)

                        .getMaps()
        );
    }



    //构造任意复杂SQL
    @Test
    public void insertSelect() throws SQLException {

        System.out.print(new SQL(executor).select("student1").column("id,name,age").eq("name", "李四").getMaps());
        new SQL(executor).delete("student1").eq("name", "李四").update();


                new SQL(executor).setSql(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                        .setValue("arg1", SQL.SELECT("student")
                                .column("id,student_id,name,age,sex,create_time")
                                .eq("name", "李四"))
                        .update();

        System.out.print(new SQL(executor).select("student1").column("id,name,age").eq("name", "李四").getMaps());
        System.out.println(new SQL(executor).setSql(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                .setValue("arg1", SQL.SELECT("student")
                        .column("id,student_id,name,age,sex,create_time")
                        .eq("name", "李四")));

    }
    //having加入参数
    @Test
    public void havingPara() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("sex")
                        .count("count")//计数
                        .min("age", "min")//最小
                        .max("age", "max")//最大
                        .avg("age", "avg")//平均
                        .groupBy("sex")
                        .having("avg(age)>=:age").setValue("age", 18)
                        .getMaps()
        );
    }
    //union------------------------------------
    @Test
    public void union() throws SQLException {

        System.out.println(
                new SQL(executor)

                        .setSql(SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .setSql(" union all")
                        .setSql( SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .setSql(" union all")
                        .setSql(SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .setSql(" order by age")
                        .getMaps()
        );
    }

}

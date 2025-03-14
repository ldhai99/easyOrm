package io.github.ldhai99.easyOrm.test.dbutils;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class SetValueSubSql {
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

    //setSql
    @Test
    public void setSql() throws SQLException {

        System.out.println(
                new SQL(con).addSql("select name,age from student where age = ?", 18).getMaps()
        );
    }

    //setSql+setValue
    @Test
    public void setSqlSetValue() throws SQLException {

        System.out.println(
                new SQL(con)
                        .addSql("select name,age from student where age = :arg1")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(con)
                        .addSql("select name,age from student where age = :arg1")
                        .setValue("arg1", 18).getMaps()
        );
    }
    @Test
    public void setSqlSetValue$() throws SQLException {

        System.out.println(
                new SQL(con)
                        .addSql("select name,age from student where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(con)
                        .addSql("select name,age from student where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
                        .getMaps()
        );
    }

    @Test
    public void setSqlSetSql() throws SQLException {

        System.out.println(
                new SQL(con)
                        .addSql("select :field").setValue$("field","name,age")
                        .addSql(" from :table").setValue$("table","student")
                        .addSql(" where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)
        );
        System.out.println(
                new SQL(con)
                        .addSql("select :field").setValue$("field","name,age")
                        .addSql(" from :table").setValue$("table","student")
                        .addSql(" where :arg0 = :arg1")
                        .setValue$("arg0", "age")
                        .setValue("arg1", 18)

                        .getMaps()
        );
    }



    //构造任意复杂SQL
    @Test
    public void insertSelect() throws SQLException {

        System.out.print(new SQL(con).select("student1").column("id,name,age").eq("name", "李四").getMaps());
        new SQL(con).delete("student1").eq("name", "李四").update();


                new SQL(con).addSql(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                        .setValue("arg1", SQL.SELECT("student")
                                .column("id,student_id,name,age,sex,create_time")
                                .eq("name", "李四"))
                        .update();

        System.out.print(new SQL(con).select("student1").column("id,name,age").eq("name", "李四").getMaps());
        System.out.println(new SQL(con).addSql(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                .setValue("arg1", SQL.SELECT("student")
                        .column("id,student_id,name,age,sex,create_time")
                        .eq("name", "李四")));

    }
    //having加入参数
    @Test
    public void havingPara() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("sex")
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
                new SQL(con)

                        .addSql(SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .addSql(" union all")
                        .addSql( SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .addSql(" union all")
                        .addSql(SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .addSql(" order by age")
                        .getMaps()
        );
    }

}

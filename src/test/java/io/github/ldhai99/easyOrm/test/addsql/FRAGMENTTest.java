package io.github.ldhai99.easyOrm.test.addsql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.base.SqlKey;
import io.github.ldhai99.easyOrm.dynamic.DynamicSQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public  class FRAGMENTTest {

    //setSql
    @Test
    public void setSql() throws SQLException {

        System.out.println(
                SQL.ADDSQL("select name,age from student where age = ?", 18).getMaps()
        );
        System.out.println(
                DynamicSQL.ADDSQL("select name,age from student where age = ?", 18).getMaps()
        );
    }

    //setSql+setValue
    @Test
    public void setSqlSetValue() throws SQLException {

        System.out.println(
                SQL.ADDSQL("select name,age from student where age = :arg1")
                        .setParameter("arg1", 18)
        );
        System.out.println(
                SQL.ADDSQL("select name,age from student where age = :arg1")
                        .setParameter("arg1", 18).getMaps()
        );
    }
    @Test
    public void setSqlSetValue$() throws SQLException {

        System.out.println(
                SQL.ADDSQL("select name,age from student where :arg0 = :arg1")
                        .setParameter$("arg0", "age")
                        .setParameter("arg1", 18)
        );
        System.out.println(
                 SQL.ADDSQL("select name,age from student where :arg0 = :arg1")
                        .setParameter$("arg0", "age")
                        .setParameter("arg1", 18)
                        .getMaps()
        );
    }

    @Test
    public void setSqlSetSql() throws SQLException {

        System.out.println(
                SQL.ADDSQL("select :field").setParameter$("field","name,age")
                        .addSql(" from :table").setParameter$("table","student")
                        .addSql(" where :arg0 = :arg1")
                        .setParameter$("arg0", "age")
                        .setParameter("arg1", 18)
        );
        System.out.println(
                SQL.ADDSQL("select :field").setParameter$("field","name,age")
                        .addSql(" from :table").setParameter$("table","student")
                        .addSql(" where :arg0 = :arg1")
                        .setParameter$("arg0", "age")
                        .setParameter("arg1", 18)

                        .getMaps()
        );
    }



    //构造任意复杂SQL
    @Test
    public void insertSelect() throws SQLException {

        System.out.print(new SQL().select("student1").column("id,name,age").eq("name", "李四").getMaps());
       SQL.DELETE("student1").eq("name", "李四").update();


        SQL.ADDSQL(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                .setParameter("arg1", SQL.SELECT("student")
                        .column("id,student_id,name,age,sex,create_time")
                        .eq("name", "李四"))
                .update();

        System.out.print(SQL.SELECT("student1").column("id,name,age").eq("name", "李四").getMaps());
        System.out.println(SQL.ADDSQL(" insert into student1(id,student_id,name,age,sex,create_time) :arg1")
                .setParameter("arg1", SQL.SELECT("student")
                        .column("id,student_id,name,age,sex,create_time")
                        .eq("name", "李四")));

    }
    //union------------------------------------
    @Test
    public void union() throws SQLException {

        System.out.println(
                SQL.ADDSQL(SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .addSql(SqlKey.UNION_ALL)
                        .addSql( SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .addSql(SqlKey.UNION_ALL)
                        .addSql(SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .addSql(" order by age")
                        .getMaps()
        );
    }
    //union------------------------------------
    @Test
    public void sqlkey() throws SQLException {

        System.out.println(
                SQL.ADDSQL(SqlKey.SELECT)
                        .addSql(" name,age")
                        .addSql(SqlKey.FROM)
                        .addSql(" student")
                        .addSql(SqlKey.WHERE)
                        .addSql("age")
                        .addSql(SqlKey.EQ)
                        .addSql("18")
                        .getMaps()

        );
        System.out.println(
                SQL.ADDSQL(SqlKey.SELECT)
                        .addSql(" name,age")
                        .addSql(SqlKey.FROM)
                        .addSql(" student")
                        .addSql(SqlKey.WHERE)
                        .addSql(SqlKey.WHERE)
                        .addSql("age")
                        .addSql(SqlKey.EQ)
                        .addSql("18")


        );
    }
}

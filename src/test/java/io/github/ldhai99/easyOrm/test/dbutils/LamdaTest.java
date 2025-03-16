package io.github.ldhai99.easyOrm.test.dbutils;

import io.github.ldhai99.easyOrm.Lambda.JoinOn;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.test.Student1;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class LamdaTest {

    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DbTools.getConnection();
        ds = DbTools.getDataSource();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    //获取单个值------------------------------------
    @Test
    public void eq() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public void eqlamda() throws SQLException {

        System.out.println(
                new SQL(con).select(Student.class).column(Student::getName,Student::getAge).eq( Student::getAge, 18).getMaps()
        );
    }
    @Test
    public void queryStudent() throws SQLException{

        Student student =  new SQL(con).select("student").eq("age", 18).getBean(Student.class);
        System.out.println( student);
    }
    @Test
    public  void joinlamda() throws SQLException {

        System.out.println(
                new SQL(con).select(Student.class).fullColumn(Student::getAge).fullColumn(Student1::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
        System.out.println(
                new SQL(con).select(Student.class).fullColumn(Student::getAge,Student::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
        System.out.println(
                new SQL(con).select(Student.class).fullColumn(Student::getAge,Student::getName)
                        .join(Student1.class,SQL.ADDSQLFullColumn(Student1::getName).addSql("=").addSqlFullColumn(Student::getName))
                        .getMaps()
        );
    }
    @Test
    public  void join0() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("student.age,student1.name")
                        .join("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
}

package io.github.ldhai99.easyOrm.test.lamda;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
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
        con = DataSourceManager.getConnection();
        ds = DataSourceManager.getDataSource();
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

    //测试@TableField注解---在sex 字段上有@TableField注解
    @Test
    public void eqlamdaTableField() throws SQLException {

        System.out.println(
                new SQL(con).select(Student.class).column(Student::getName,Student::getAge,Student::getSex)
                        .eq( Student::getAge, 18).getMaps()
        );

    }
    @Test
    public void eqlamdaFullTableField() throws SQLException {

        System.out.println(
                SQL.SELECT(Student.class).column(Student::getName,Student::getAge,Student::getSex)
                        .eq( Student::getAge, 18)
        );
        System.out.println(
                SQL.SELECT(Student.class).column(Student::getName,Student::getAge,Student::getSex)
                        .eq( Student::getAge, 18).getMaps()
        );
    }
    //测试实体中字段与表字段不一致
    @Test
    public void queryStudent() throws SQLException{

        Student student =  SQL.SELECT("student").eq("age", 18).getBean(Student.class);
        System.out.println( student);

        student =  new SQL(con).select("student").eq("age", 18).getBean(Student.class);
        System.out.println( student);
    }
}

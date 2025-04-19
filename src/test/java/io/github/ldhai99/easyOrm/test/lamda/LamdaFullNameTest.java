package io.github.ldhai99.easyOrm.test.lamda;

import io.github.ldhai99.easyOrm.Lambda.JoinOn;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.test.Student1;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class LamdaFullNameTest {

    @Test
    public  void join0() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("student.age,student1.name")
                        .join("student1", "student1.name=student.name")
                        .getMaps()
        );
    }
    @Test
    public  void joinlamda() throws SQLException {

        System.out.println(
                SQL.SELECT(Student.class).fullCol(Student::getAge).fullCol(Student1::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT(Student.class).fullCol(Student::getAge,Student::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT(Student.class).fullCol(Student::getAge,Student::getName)
                        .join(Student1.class,SQL.ADDSQLFULLCOL(Student1::getName).addSql("=").addSqlFullCol(Student::getName))
                        .getMaps()
        );
    }
}

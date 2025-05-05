package io.github.ldhai99.easyOrm.test.lamda;

import io.github.ldhai99.easyOrm.join.JoinOn;
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
                SQL.SELECT(Student.class).full(Student::getAge).full(Student1::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT(Student.class).full(Student::getAge,Student::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );

    }
    @Test
    public  void joinlamdaAdd() throws SQLException {

        System.out.println(
                SQL.SELECT(Student.class).full(Student::getAge,Student::getName)
                        .join(Student1.class,SQL.ADDFull(Student1::getName).addSql("=").addFull(Student::getName))

        );
        System.out.println(
                SQL.SELECT(Student.class).full(Student::getAge,Student::getName)
                        .join(Student1.class,SQL.ADDFull(Student1::getName).addSql("=").addFull(Student::getName))
                        .getMaps()
        );
    }
    @Test
    //联合多表查询，用lambda表达式，全名（表名.字段名称）,on条件是全名（表名.字段名称）
    public  void joinOnLamda() throws SQLException {
        System.out.println(
                SQL.SELECT(Student.class).full(Student::getAge).full(Student1::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())

        );
        System.out.println(
                SQL.SELECT(Student.class).full(Student::getAge).full(Student1::getName)
                        .join(Student1.class, JoinOn.on(Student1::getName).eq(Student::getName).toString())
                        .getMaps()
        );
    }
}

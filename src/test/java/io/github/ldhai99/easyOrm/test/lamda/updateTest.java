package io.github.ldhai99.easyOrm.test.lamda;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;

public class updateTest {
    @Test
    public void updateTest() throws SQLException {

        System.out.println(
                SQL.SELECT(Student.class)
                        .eq(Student::getName, "李四")
                        .getMaps());

        SQL.DELETE(Student.class)
                .eq(Student::getName,"李四")
                .update();

        SQL.INSERT(Student.class)
                .set(Student::getId,2).set(Student::getAge,19)
                .set(Student::getName,"李四").set(Student::getSex,"女")
                .set(Student::getStudentId,"20190102")
                .set(Student::getPassword,"666").set(Student::getCreateTime,new Date())
                .update();

        System.out.println(
                SQL.SELECT(Student.class)
                        .eq(Student::getName, "李四")
                        .getMaps());

        SQL.UPDATE(Student.class)
                .set(Student::getAge, 29)
                .eq(Student::getName, "李四")
                .update();

        System.out.println(
                SQL.SELECT(Student.class)
                        .eq(Student::getName, "李四")
                        .getMaps());
        SQL.UPDATE(Student.class)
                .set(Student::getAge, 18)
                .eq(Student::getName, "李四")
                .update();

        System.out.println(
                SQL.SELECT(Student.class)
                        .eq(Student::getName, "李四")
                        .getMaps());
        // connection.commit();
    }
}

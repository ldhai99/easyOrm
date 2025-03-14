package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.executor.Executor;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateNameQueryTest {

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
    public void updateStudentTest() throws SQLException{
        Student student = null;

        student = queryStudent("李四");
        System.out.println(student);

        deleteStudentByName("李四");

        //增加学生
        student = new Student();
        student.setId(0);
        student.setAge(19);
        student.setName("李四");
        student.setPassword("666");
        student.setSex("女");
        student.setStudentId("20190102");
        student.setCreateTime(new Date());
        insert(student);

        student = queryStudent("李四");
        System.out.println(student);

        //修改学生
        student.setAge(18);
        updateStudent(student);
        System.out.println(queryStudent("李四"));
    }

    public int updateStudent(Student student) throws SQLException{
        String sql = "update student1 set age = :age where id = :id";
        return new SQL(executor).addSql(sql).setValue("id",student.getId())
                .setValue("age",student.getAge())
                .update();
    }

    public int deleteStudentByName(String name) throws SQLException {
        String sql = "delete from student1 where name = :name";
        return new SQL(executor).addSql(sql)
                .setValue("name",name)
                .update();
    }

    public int insert(Student student) throws SQLException{
        String sql = "INSERT  INTO student1(id,student_id,name,password,sex,age, create_time) " +
                "values (:id,:student_id,:name,:password,:sex,:age, :create_time)";
        return new SQL(executor).addSql(sql)
                .setValue("id",student.getId())
                .setValue("student_id",student.getStudentId())
                .setValue("name",student.getName())
                .setValue("password",student.getPassword())
                .setValue("sex",student.getSex())
                .setValue("age",student.getAge())
                .setValue("create_time",student.getCreateTime())
                .update();
    }


    public Student queryStudent(String name) throws SQLException{
        Student student = (Student)new SQL(executor).addSql("select * from student1 where name=:name")
                .setValue("name",name)
                .getBean(Student.class);
        return student;
    }
}

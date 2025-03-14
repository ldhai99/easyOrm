package io.github.ldhai99.easyOrm.test.dbutils;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.test.Student;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;


public class UpdateQueryTest {
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
        String sql = "update student1 set age = ? where id = ?";
        Object[] args = {student.getAge(), student.getId()};
        return new SQL(con).setSql(sql,args).update();
    }

    public int deleteStudentByName(String name) throws SQLException {
        String sql = "delete from student1 where name = ?";
        Object[] args = {name};
        return new SQL(con).setSql(sql,args).update();
    }

    public int insert(Student student) throws SQLException{
        String sql = "INSERT  INTO student1(id,student_id,name,password,sex,age, create_time) values (?,?,?,?,?,?,?)";
        Object[] args = {student.getId(), student.getStudentId(), student.getName(), student.getPassword(), student.getSex(), student.getAge(), student.getCreateTime()};
        return new SQL(con).setSql(sql,args).update();
    }


    public Student queryStudent(String name) throws SQLException{

        Student student = (Student) new SQL(con).setSql("select * from student1 where name=?",name).getBean(Student.class);
        return student;
    }
}

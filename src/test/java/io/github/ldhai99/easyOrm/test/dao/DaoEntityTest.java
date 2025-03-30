package io.github.ldhai99.easyOrm.test.dao;

import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.test.studentDao.dao2.StudentDao;
import io.github.ldhai99.easyOrm.tools.ChainMap;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.Date;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DaoEntityTest {
    StudentDao dao;

    @BeforeAll
    public void getTemplate() {
        DbTools.getExecutor();

        dao = new StudentDao();
    }

    @AfterEach
    public void afterTest() throws SQLException {


    }
    @Test
    public void updateEntityTestall() throws SQLException {

        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
        );

        dao.deleteByMap(ChainMap.of("name","李四"));
        queyrTest();
        insertEntityTest();

        dao.updateByMap(ChainMap.of("age", 29),ChainMap.of("name", "李四"));


        queyrTest();

        updateEntityTest();

        queyrTest();
        // connection.commit();
    }
    public  void queyrTest()  {
        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
        );

    }
    public void  insertEntityTest()  {
        Student student = new Student();
        student.setId(2);
        student.setName("李四");
        student.setAge(19);
        student.setSex("女");
        student.setStudentId("20190102");
        student.setPassword("666");
        student.setCreateTime(new Date());


        dao.insert(student);
    }
    public void  updateEntityTest()  {
        Student student1 = new Student();
        student1.setId(2);
        student1.setAge(19);
        dao.update(student1);
    }
}

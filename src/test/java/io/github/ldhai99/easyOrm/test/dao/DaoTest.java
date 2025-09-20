package io.github.ldhai99.easyOrm.test.dao;

import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.test.studentDao.dao2.StudentDao;
import io.github.ldhai99.easyOrm.tools.ChainMap;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DaoTest {
    StudentDao dao;

    @BeforeAll
    public void getTemplate() {
        DataSourceManager.getExecutor();

        dao = new StudentDao();
    }

    @AfterEach
    public void afterTest() throws SQLException {


    }

    //增加------------------------
    @Test
    public void insertTest() throws SQLException {

        System.out.println(
                dao.getMapsByMap(ChainMap.of("name", "李四"))
        );
        dao.deleteByMap(ChainMap.of("name", "李四"));

        System.out.println(
                dao.insert(ChainMap.of()
                        .set("id", 0).set("age", 19).set("name", "李四").set("sex", "女")
                        .set("student_id", "20190102").set("password", "666").set("create_time", new Date()))
        );
        System.out.println(
                dao.getMapsByMap(ChainMap.of("name", "李四"))
        );
    }

    @Test
    public void updateTest() throws SQLException {

        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
                        );

        dao.deleteByMap(ChainMap.of("name","李四"));

       dao.insert(ChainMap.of()
               .set("id",2)
               .set("age",19)
               .set("name","李四")
               .set("sex","女")
               .set("student_id","20190102")
               .set("password","666")
               .set("create_time",new Date()));


        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
        );

        dao.updateByMap(ChainMap.of("age", 29),ChainMap.of("name", "李四"));


        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
        );
        dao.updateByMap(ChainMap.of("age", 19),ChainMap.of("name", "李四"));

        System.out.println(
                dao.getMapByMap(ChainMap.of("name", "李四"))
        );
        // connection.commit();
    }

    //column子查询扩展------------------------
    @Test
    public void existsId() throws SQLException {

        System.out.println(
                dao.exists("1")
        );
    }
    @Test
    public void existsmap() throws SQLException {

        System.out.println(
                dao.existsByMap(ChainMap.of("name", "李四"))
        );
    }

    //查询------------------------

    @Test
    public void getMapById() throws SQLException {

        System.out.println(
                dao.getMapById("1")
        );
    }

    @Test
    public void getMapsByIds1() throws SQLException {

        System.out.println(
                dao.getMapsByIds(1, 2, 3)
        );
    }

    @Test
    public void getMapsByIds2() throws SQLException {
        Object[] objects = {1, 2, 3};
        System.out.println(

                dao.getMapsByIds(objects)
        );
    }

    @Test
    public void getMapsByIds3() throws SQLException {
        List objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);
        System.out.println(

                dao.getMapsByIds(objects)
        );
    }

    @Test
    public void getMapsByMap() throws SQLException {

        System.out.println(

                dao.getMapsByMap(ChainMap.of("age", 18).set("sex", "男"))
        );
    }
    //计数
    @Test
    public void getCount() throws SQLException {

        System.out.println(

                dao.getCountByMap(ChainMap.of("age", 18).set("sex", "男"))
        );
    }

    //翻页
    @Test
    public void getPage() throws SQLException {

        System.out.println(

                dao.pageMapsBySql(new PageModel().setCurrent(3).setSize(2)
                        ,null)
        );
    }

}

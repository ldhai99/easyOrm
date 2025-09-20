package io.github.ldhai99.easyOrm.test.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.test.Student;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageGetDataTest {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DataSourceManager.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }
    //获取list maps
    @Test
    public void pageSqlMaps() throws SQLException {

        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .pageMaps());

    }
    //先返回PAGE,获取list maps
    @Test
    public void pageSqlBYMaps() throws SQLException {

        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .pageMapsAnd().getRecordsMaps());

    }
    //获取list beans
    @Test
    public void pageSqlBeans() throws SQLException {

        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .pageBeans(Student.class));

    }
    //先返回PAGE，获取list beans
    @Test
    public void pageSqlByBeans() throws SQLException {

        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
        List<Student> students=PAGE.of(sql)
                .setCurrent(1)
                .setSize(2)
                .pageBeansAnd(Student.class).getRecords();

        System.out.println(students     );

    }
}

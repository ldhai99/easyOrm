package io.github.ldhai99.easyOrm.test.set;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;


import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.Date;

import static io.github.ldhai99.easyOrm.executor.ExecutorManager.getExecutor;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InsertTest {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }
    @Test
    public void insertTest() throws SQLException {

        System.out.println(
                new SQL(executor).select("student1").eq("name", "李四").getMaps());

        new SQL(executor)
                .delete("student1")
                .eq("name","李四")
                .update();

        System.out.println(
        new SQL(executor)
                .insert("student1").set("id",0).set("age",19).set("name","李四").set("sex","女")
                .set("student_id","20190102").set("password","666").set("create_time",new Date()).insert()
                );
        System.out.println(
                new SQL(executor).select("student1").eq("name", "李四").getMaps());
    }
}

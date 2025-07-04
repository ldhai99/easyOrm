package io.github.ldhai99.easyOrm.test.jdbcTemplete;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ColumnSubSql {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }

    //column子查询扩展------------------------
    @Test
    public void coloumsubselect() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age")
                        .column(
                                SQL.SELECT("student1").column("name")
                                        .where("student1.name=student.name")
                                , "name1")
                        .eq("age", 18).getMaps()
        );
    }

}

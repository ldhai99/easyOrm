package io.github.ldhai99.easyOrm.test.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.page.MysqlPageSqlById;
import io.github.ldhai99.easyOrm.page.MysqlPageSqlByStartId;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageTest {
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
    public void pageSql() throws SQLException {


        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .getPageSql());

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .getPageMaps());

    }

    @Test
    public void pageSqlById() throws SQLException {


        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageSqlGenerator(new MysqlPageSqlById())
                        .getPageSql());

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageSqlGenerator(new MysqlPageSqlById())
                        .getPageMaps());

    }
    @Test
    public void pageSqlByStartId() throws SQLException {


        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageStartId(0)
                        .setPageSqlGenerator(new MysqlPageSqlByStartId())
                        .getPageSql());

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageStartId(0)
                        .setPageSqlGenerator(new MysqlPageSqlByStartId())
                        .getPageMaps());

    }
    @Test
    public void testSql() throws SQLException {


        SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");


        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageSqlGenerator(new MysqlPageSqlById())
                        .toBuild().getPageSql().toString());

    }
}

package io.github.ldhai99.easyOrm.test.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSql;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSqlById;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSqlByStartId;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageSQLGeneratorNewTest {
    private Executor executor;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }

    //正常翻页PageSqlNormal
    @Test
    public void pageSqNormal() throws SQLException {
        SQL sql = SQL.SELECT("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setPageSqlGenerator(new MysqlPageSql())
                        .getPagedSql());
        //select id,name from student order by id asc   limit 0, 2

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setPageSqlGenerator(new MysqlPageSql())
                        .pageMaps());
    }
    //有id的翻页PageSqlid
    @Test
    public void pageSqlId() throws SQLException {
        SQL sql =  SQL.SELECT("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageSqlGenerator(new MysqlPageSqlById())
                        .getPagedSql());
        //select id,name from student where  id in
        // (select id from  (    select id from student order by id asc   limit 0, 2) a )  order by id asc

        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageSqlGenerator(new MysqlPageSqlById())
                        .pageMaps());

    }
    //有起始行翻页PageSqlStartid
    @Test
    public void pageSqlByStartId() throws SQLException {
        SQL sql = SQL.SELECT("student").column("id,name").orderBy("id");
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageStartId(2)
                        .setPageSqlGenerator(new MysqlPageSqlByStartId())
                        .getPagedSql());
        //(select id,name from student where  id >  :p4e4aea350  order by id asc)    limit 2
        System.out.println(
                PAGE.of(sql)
                        .setCurrent(1)
                        .setSize(2)
                        .setCountId("id")
                        .setPageStartId(2)
                        .setPageSqlGenerator(new MysqlPageSqlByStartId())
                        .pageMaps());
    }
}

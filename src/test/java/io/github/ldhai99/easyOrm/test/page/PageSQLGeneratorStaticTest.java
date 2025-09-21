package io.github.ldhai99.easyOrm.test.page;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.config.ConfigPageSql;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;


    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class PageSQLGeneratorStaticTest {
        private Executor executor;

        private NamedParameterJdbcTemplate npjt;

        @BeforeAll
        public void getTemplate() {
            executor = DataSourceManager.getExecutor();

        }

        @AfterEach
        public void afterTest() throws SQLException {

        }
        //默认PageSql
        @Test
        public void pageSql() throws SQLException {


            SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .getPagedSql());
            //(select id,name from student order by id asc)   limit 0, 2

            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .pageMaps());

        }
        //正常翻页PageSqlNormal
        @Test
        public void pageSqNormal() throws SQLException {


            SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)

                            .setPageSqlGenerator(ConfigPageSql.getPageSqlNormal())
                            .getPagedSql());
            //(select id,name from student order by id asc)   limit 0, 2

            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)

                            .setPageSqlGenerator(ConfigPageSql.getPageSqlNormal())
                            .pageMaps());

        }
        //有id的翻页PageSqlid
        @Test
        public void pageSqlId() throws SQLException {


            SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .setCountId("id")
                            .setPageSqlGenerator(ConfigPageSql.getPageSqlById())
                            .getPagedSql());
            //select id,name from student where  id in
            // (select id from  (    (select id from student order by id asc)   limit 0, 2) a )  order by id asc

            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .setCountId("id")
                            .setPageSqlGenerator(ConfigPageSql.getPageSqlById())
                            .pageMaps());

        }
        //有起始行翻页PageSqlStartid
        @Test
        public void pageSqlByStartId() throws SQLException {


            SQL sql = new SQL(executor).select("student").column("id,name").orderBy("id");
            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .setCountId("id")
                            .setPageStartId(2)
                            .setPageSqlGenerator(ConfigPageSql.getPageSqlByStartId())
                            .getPagedSql());
            //(select id,name from student where  id >  :p4e4aea350  order by id asc)    limit 2


            System.out.println(
                    PAGE.of(sql)
                            .setCurrent(1)
                            .setSize(2)
                            .setCountId("id")
                            .setPageStartId(2)
                            .setPageSqlGenerator(ConfigPageSql.getPageSqlByStartId())
                            .pageMaps());

        }
    }

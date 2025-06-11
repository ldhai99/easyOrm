package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;

import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WhereSubSql {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }

    //in子查询
    @Test
    public void inSubSelect() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age")
                        .in("age", SQL.SELECT("student").column("age").in("name", "张三", "李四"))
                        .getMaps()
        );
    }

    @Test
    public void notinselect() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age")
                        .notIn("age", SQL.SELECT("student").column("age").in("name", "张三", "李四"))
                        .getMaps()
        );
    }




    //from子查询扩展------------------------

    @Test
    public void fromSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).column("name,sex")
                        .from(
                                SQL.SELECT("student").column("*"), "a")
                        .eq("age", 18).getMaps()
        );
    }

    @Test
    public void isSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .isNotNull( SQL.SELECT("student1").column("age").eq("name","李四")
                        ).eq("name","李四").getMaps()
        );
    }







}

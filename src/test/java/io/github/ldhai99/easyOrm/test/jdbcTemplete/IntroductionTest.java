package io.github.ldhai99.easyOrm.test.jdbcTemplete;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;


import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;

import static io.github.ldhai99.easyOrm.executor.ExecutorManager.getExecutor;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntroductionTest {

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
    public  void chainType() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void factory() throws SQLException {
        //静态工厂方法
        System.out.println(
                SQL.ofExecutor(executor).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void executor() throws SQLException {
        //传入执行器
        System.out.println(
                SQL.ofExecutor(executor).select("student").column("name,age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void getint() throws SQLException {
        //获取李四的年龄
        System.out.println(
                SQL.ofExecutor(executor).select("student").column("age").eq("name","李四").getInteger()
        );

    }
    @Test
    public  void notOrder() throws SQLException {

        System.out.println(
                new SQL(executor).eq("age", 18).column("name,age").select("student").getMaps()
        );
    }
    @Test
    public  void manytimes() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name").column("age").eq("age", 18).getMaps()
        );
    }
    @Test
    public  void preventInjection() throws SQLException {

        System.out.println(
                new SQL(executor).select("student").column("name,age").eq("age", 18)
        );
    }
}

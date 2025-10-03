package io.github.ldhai99.easyOrm.test.set;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetSubSql {
    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DataSourceManager.getExecutor();

    }

    @AfterEach
    public void afterTest() throws SQLException {

    }
    @Test
    public void updateSetSql() throws SQLException {

        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());

        new SQL(executor).update(" student ")
                .set("age", SQL.ADDSQL("round(?)", 19.4))
                .eq("name", "李四").update();

        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
    }
    //更新中支持子set查询
    @Test
    public void updateSetSelect() throws SQLException {

        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(executor).update("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(new SQL(executor).select("student1").column("age").eq("name", "李四").getMaps());

        new SQL(executor).update(" student ")
                .set("age", SQL.SELECT("student1").column("age").eq("name", "李四"))
                .eq("name", "李四").update();

        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(executor).update("student1").set("age", 19).eq("name", "李四").update();
    }



    @Test
    public void updateJoin() throws SQLException {

        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(executor).update("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(new SQL(executor).select("student1").column("age").eq("name", "李四").getMaps());

        new SQL(executor).update(" student ")
                .join("student1", "student1.name=student.name")
                .set("student.age", SQL.ADDSQL("student1.age"))
                .eq("student.name", "李四").update();

        System.out.print(new SQL(executor).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(
                new SQL(executor).update(" student ")
                        .leftJoin("student1", "student1.name=student.name")
                        .set("student.age", SQL.ADDSQL("student1.age"))
                        .eq("student.name", "李四")
        );
        new SQL(executor).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(executor).update("student1").set("age", 19).eq("name", "李四").update();
    }

}

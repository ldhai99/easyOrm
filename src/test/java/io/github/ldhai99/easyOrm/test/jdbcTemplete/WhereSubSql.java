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
    @Test
    public void whereSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age").where(
                        SQL.ADDSql("age = round(?)", 18.3)
                ).getMaps()
        );
        System.out.println(
                new SQL(executor).select(" student ").column("name,age").where(
                        SQL.ADDSql("age = round(:age)").setValue("age", 18.3)
                ).getMaps()
        );
    }
    @Test
    public void eqSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .eq("age", SQL.ADDSql("round(?)", 18.3)
                        ).getMaps()
        );
        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .eq("age", SQL.ADDSql("round(:age)").setValue("age", 18.3)
                        ).getMaps()
        );

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .eq("age"
                                , SQL.SELECT("dual").column("round(:age)").setValue("age", 18.3)
                        ).getMaps()
        );


    }
    @Test
    public void nameSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .eq(SQL.ADDSql("round(age,:arg0)").setValue("arg0", 2)
                                , SQL.SELECT("dual").column("round(:age)").setValue("age", 18.3)
                        ).getMaps()
        );
    }

    @Test
    public void likeSubSql() throws SQLException {


        System.out.println(
                new SQL(executor).select("student").column("name,sex")
                        .like("sex", SQL.ADDSql(" CONCAT('%',right(?,1),'%')", "男女")).getMaps()
        );
        System.out.println(
                new SQL(executor).select("student").column("name,sex")
                        .like("sex", SQL.ADDSql(" CONCAT('%',left(:sex,1),'%')").setValue("sex", "男女")).getMaps()
        );
        System.out.println(
                new SQL(executor).select("student").column("name,sex")
                        .like("sex", SQL.SELECT("dual").column(" CONCAT('%',left(:sex,1),'%')").setValue("sex", "男女")).getMaps()
        );
    }

    @Test
    public void betweenSubSql() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSql("round(?)", 18.3)
                                , SQL.ADDSql("round(?)", 20.3)
                        ).getMaps()
        );
        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSql("round(:age)").setValue("age", 18.3)
                                , SQL.ADDSql("round(:age)").setValue("age", 20.3)
                        ).getMaps()
        );

        System.out.println(
                new SQL(executor).select(" student ").column("name,age")
                        .between("age"
                                , SQL.SELECT("dual").column("round(:age)").setValue("age", 18.3)
                                , SQL.SELECT("dual").column("round(:age)").setValue("age", 20.3)
                        ).getMaps()
        );


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


    //比较谓词支持子查询
    @Test
    public void setFunc() throws SQLException {

        System.out.println(
                new SQL(executor).select(" student ").column("name,age").where(
                        SQL.ADDSql("age = :age").
                                setValue("age", SQL.ADDSql("round(:age)").setValue("age", 18.3))
                ).getMaps()
        );
    }





}

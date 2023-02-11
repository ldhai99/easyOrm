package com.github.ldhai99.easyOrm.easyOrm.dbutils;


import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.tools.DruidUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SetSubSql {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DruidUtils.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }
    @Test
    public void updateSetSql() throws SQLException {

        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());

        new SQL(con).update(" student ")
                .set("age", SQL.SETSQL("round(?)", 19.4))
                .eq("name", "李四").update();

        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
    }
    //更新中支持子set查询
    @Test
    public void updateSetSelect() throws SQLException {

        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(con).update("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(new SQL(con).select("student1").column("age").eq("name", "李四").getMaps());

        new SQL(con).update(" student ")
                .set("age", SQL.SELECT("student1").column("age").eq("name", "李四"))
                .eq("name", "李四").update();

        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(con).update("student1").set("age", 19).eq("name", "李四").update();
    }



    @Test
    public void updateJoin() throws SQLException {

        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(con).update("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(new SQL(con).select("student1").column("age").eq("name", "李四").getMaps());

        new SQL(con).update(" student ")
                .join("student1", "student1.name=student.name")
                .set("student.age", SQL.SETSQL("student1.age"))
                .eq("student.name", "李四").update();

        System.out.print(new SQL(con).select("student").column("age").eq("name", "李四").getMaps());
        System.out.print(
                new SQL(con).update(" student ")
                        .leftJoin("student1", "student1.name=student.name")
                        .set("student.age", SQL.SETSQL("student1.age"))
                        .eq("student.name", "李四")
        );
        new SQL(con).update("student").set("age", 18).eq("name", "李四").update();
        new SQL(con).update("student1").set("age", 19).eq("name", "李四").update();
    }

}

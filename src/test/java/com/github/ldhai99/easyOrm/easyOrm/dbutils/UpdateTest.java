package com.github.ldhai99.easyOrm.easyOrm.dbutils;

import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.tools.DruidUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class UpdateTest {
    private DataSource ds;
    private Connection connection;

    @BeforeEach
    public void beforeTest() throws SQLException {
        connection = DruidUtils.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        connection.close();
    }
    @Test
    public void updateTest() throws SQLException {

        System.out.println(
                new SQL(connection)
                        .select("student")
                        .eq("name", "李四")
                        .getMaps());

        new SQL(connection)
                .delete("student")
                .eq("name","李四")
                .update();

        new SQL(connection)
                .insert("student")
                .set("id",0).set("age",19)
                .set("name","李四").set("sex","女")
                .set("student_id","20190102")
                .set("password","666").set("create_time",new Date())
                .update();

        System.out.println(
                new SQL(connection).select("student")
                        .eq("name", "李四")
                        .getMaps());

        new SQL(connection)
                .update("student")
                .set("age", 29)
                .eq("name", "李四")
                .update();

        System.out.println(
                new SQL(connection).select("student")
                        .eq("name", "李四")
                        .getMaps());
        new SQL(connection)
                .update("student")
                .set("age", 18)
                .eq("name", "李四")
                .update();

        System.out.println(
                new SQL(connection).select("student")
                        .eq("name", "李四")
                        .getMaps());
       // connection.commit();
    }
}

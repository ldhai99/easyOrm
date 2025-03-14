package io.github.ldhai99.easyOrm.test.dbutils;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;


public class InsertTest {
    private DataSource ds;
    private Connection connection;

    @BeforeEach
    public void beforeTest() throws SQLException {
        connection = DbTools.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        connection.close();
    }
    @Test
    public void insertTest() throws SQLException {

        System.out.println(
                new SQL(connection).select("student1").eq("name", "李四").getMaps());

        new SQL(connection)
                .delete("student1")
                .eq("name","李四")
                .update();

        System.out.println(
        new SQL(connection)
                .insert("student1").set("id",0).set("age",19).set("name","李四").set("sex","女")
                .set("student_id","20190102").set("password","666").set("create_time",new Date()).insert()
                );
        System.out.println(
                new SQL(connection).select("student1").eq("name", "李四").getMaps());
    }
}

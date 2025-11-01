package io.github.ldhai99.easyOrm.test.dbutils;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.config.EasyOrmConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TaskTest {

    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = EasyOrmConfig.getConnection();
        ds = EasyOrmConfig.getDataSource();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    //获取单个值------------------------------------
    @Test
    public void taskSelect() throws SQLException {

        System.out.println(
                new SQL(ds).select("student").column("10.3333").eq("age", 18).getInteger()
        );
        System.out.println(
                new SQL(ds).where().eq("age", 18).getInteger()
        );

    }
}

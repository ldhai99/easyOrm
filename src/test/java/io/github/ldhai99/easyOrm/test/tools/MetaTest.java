package io.github.ldhai99.easyOrm.test.tools;

import io.github.ldhai99.easyOrm.tools.DbInfo;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MetaTest {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DbTools.getConnection();
    }
    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    @Test
    public  void test() throws SQLException{
        System.out.println(DbInfo.getColumnListNames(con,"student"));
        System.out.println(DbInfo.getColumnListNamesExcept(con,"student","id"));
        System.out.println(DbInfo.getColumnNames(con,"student"));
        System.out.println(DbInfo.getColumnNamesExcept(con,"student","id"));
    }

}

package io.github.ldhai99.easyOrm.test.dbutils;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ColumnSubSql {

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

    //column子查询扩展------------------------
    @Test
    public void coloumsubselect() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .column(
                                SQL.SELECT("student1").column("name")
                                        .where("student1.name=student.name")
                                , "name1")
                        .eq("age", 18).getMaps()
        );
    }

    @Test
    public void columeSetSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age").
                        column(SQL.ADDSQL("CASE WHEN age>=? THEN '成年' ELSE '未成年' END", 18), "adult")
                        .in("age", 17, 18)

                        .getMaps()
        );
        System.out.println(
                new SQL(con).select(" student ").column("name,age").
                        column(SQL.ADDSQL("CASE WHEN age>=:age THEN '成年' ELSE '未成年' END").setValue("age", 18), "adult")
                        .in("age", 17, 18)

                        .getMaps()
        );
    }
}

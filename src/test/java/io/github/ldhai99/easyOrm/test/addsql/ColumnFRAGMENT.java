package io.github.ldhai99.easyOrm.test.addsql;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ColumnFRAGMENT {

    @Test
    public void columeSetSql() throws SQLException {

        System.out.println(
                SQL.SELECT(" student ").column("name,age").
                        column(SQL.ADDSQL("CASE WHEN age>=? THEN '成年' ELSE '未成年' END", 18), "adult")
                        .in("age", 17, 18)

                        .getMaps()
        );
        System.out.println(
                SQL.SELECT(" student ")
                        .column("name,age").
                        column(SQL.ADDSQL("CASE WHEN age>=:age THEN '成年' ELSE '未成年' END").setParameter("age", 18), "adult")
                        .in("age", 17, 18)

                        .getMaps()
        );
    }
}

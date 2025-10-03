package io.github.ldhai99.easyOrm.test.where;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class WhereEmpty {
    //测试没条件时候也可以处理
    @Test
    public void tt() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age")

        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .getMaps()
        );
    }
}

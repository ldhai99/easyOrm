package io.github.ldhai99.easyOrm.test.enumtest;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.dbenum.DbEnum;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class Enumtest {
    @Test
    public void enumAge() throws SQLException {
        System.out.println(AgeFlag.AGE18.getValue());
        System.out.println(AgeFlag.AGE18.getDescription());
        System.out.println(DbEnum.valueOf(AgeFlag.class,18));

        System.out.println(
                SQL.SELECT("student")
                        .eq("age", AgeFlag.AGE18)
                        .getMap()
        );
    }
    @Test
    public void enumName() throws SQLException {

        System.out.println(
                SQL.SELECT("student")
                        .eq("name", NameEnum.LI_SI)
                        .getMap()
        );
    }

}

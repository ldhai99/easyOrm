package io.github.ldhai99.easyOrm.test.where;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ValueTypeTest {
    @Test
    public void testage() throws SQLException {

        System.out.println(
                SQL.SELECT("student")

                        .eq("age",18)
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student")

                        .eq("age","18")
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student")

                        .eq("age",new Integer(18))
                        .getMaps()
        );
    }
    @Test
    public void testPassword() throws SQLException {

        System.out.println(
                SQL.SELECT("student")

                        .eq("password",888)
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student")

                        .eq("password","888")
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student")

                        .eq("password",new Integer(888))
                        .getMaps()
        );
    }
}

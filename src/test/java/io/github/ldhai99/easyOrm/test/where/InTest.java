package io.github.ldhai99.easyOrm.test.where;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InTest {
    @Test
    public void in() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",17,18,19).getMaps()
        );
    }
    @Test
    public void in2() throws SQLException {

        Object[] objects={17,18,19};
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",objects).getMaps()
        );
    }

    @Test
    public void in3() throws SQLException {

        List objects=new ArrayList<>();
        objects.add(17);
        objects.add(18);
        objects.add(19);
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",objects)
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",objects).getMaps()
        );
    }
    @Test
    public void in4() throws SQLException {

        String objects="17,18,19";
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",objects)
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("age",objects).getMaps()
        );
    }
    @Test
    public void notin() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .notIn("age",17,18,19).getMaps()
        );
    }


}

package io.github.ldhai99.easyOrm.test.where;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InString {
    @Test
    public void in() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name","张三","李四").getMaps()
        );
    }
    @Test
    public void in2() throws SQLException {

        Object[] objects={"张三","李四"};
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name",objects).getMaps()
        );
    }

    @Test
    public void in3() throws SQLException {

        List objects=new ArrayList<>();
        objects.add("张三");
        objects.add("李四");

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name",objects)
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name",objects).getMaps()
        );
    }
    @Test
    public void in4() throws SQLException {

        String objects="张三,李四";
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name",objects)
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name",objects).getMaps()
        );
    }
    @Test
    public void notin() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .notIn("name","张三","李四").getMaps()
        );
    }
}

package io.github.ldhai99.easyOrm.test.when;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class WhereIf {
    @Test
    public void whenBoolean() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .in("name","张三","李四").getMaps()
        );

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when(1==1, where -> where.in("name","张三","李四"))
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when(1==1)
                        .then(where -> where.in("name","张三","李四"))
                        .getMaps()
        );
    }
    @Test
    public void whenLambda() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when(()->1==1, where -> where.in("name","张三","李四"))
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when(()->1==1)
                        .then(where -> where.in("name","张三","李四"))
                        .getMaps()
        );
    }
    @Test
    public void whenContext() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when((c)->c==1,1, where -> where.in("name","张三","李四"))
                        .getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,age")
                        .when((c)->c==1,1)
                        .then(where -> where.in("name","张三","李四"))
                        .getMaps()
        );
    }
}

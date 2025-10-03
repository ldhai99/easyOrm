package io.github.ldhai99.easyOrm.test.union;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class JoinTest {
    //union------------------------------------
    @Test
    public void union() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").eq("name","张三")
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .unionAll(
                                SQL.ADDSQL("select name,age from student "))
                        .orderBy("age",false)


        );
        System.out.println(
                SQL.SELECT("student").column("name,age").eq("name","张三")
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","张三"))
                        .unionAll(
                                SQL.SELECT("student").column("name,age").eq("name","李四"))
                        .unionAll(
                                SQL.ADDSQL("select name,age from student "))
                        .orderBy("age",false)

                        .getMaps()
        );
    }
}

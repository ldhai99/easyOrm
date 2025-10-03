package io.github.ldhai99.easyOrm.test.addsql;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class FRAGMENTSet {
    @Test
    public void updateSetSql() throws SQLException {

        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());

        SQL.UPDATE(" student ")
                .set("age", SQL.ADDSQL("round(?)", 19.4))
                .eq("name", "李四").update();

        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
    }
    //更新中支持子set查询
    @Test
    public void updateSetSelect() throws SQLException {

        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        SQL.UPDATE("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
        System.out.print(SQL.SELECT("student1").column("age").eq("name", "李四").getMaps());

        SQL.UPDATE(" student ")
                .set("age", SQL.SELECT("student1").column("age").eq("name", "李四"))
                .eq("name", "李四").update();

        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        SQL.UPDATE("student1").set("age", 19).eq("name", "李四").update();
    }



    @Test
    public void updateJoin() throws SQLException {

        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        SQL.UPDATE("student1").set("age", 19).eq("name", "李四").update();

        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
        System.out.print(SQL.SELECT("student1").column("age").eq("name", "李四").getMaps());

        SQL.UPDATE(" student ")
                .join("student1", "student1.name=student.name")
                .set("student.age", SQL.ADDSQL("student1.age"))
                .eq("student.name", "李四").update();

        System.out.print(SQL.SELECT("student").column("age").eq("name", "李四").getMaps());
        System.out.print(
                SQL.UPDATE(" student ")
                        .leftJoin("student1", "student1.name=student.name")
                        .set("student.age", SQL.ADDSQL("student1.age"))
                        .eq("student.name", "李四")
        );
        SQL.UPDATE("student").set("age", 18).eq("name", "李四").update();
        SQL.UPDATE("student1").set("age", 19).eq("name", "李四").update();
    }
}

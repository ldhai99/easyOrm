package io.github.ldhai99.easyOrm.test.where;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class LikeTest {
    //中间一致------------------------------------
    @Test
    public void like() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").like("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").like("name", "李").getMaps()
        );
    }
    @Test
    public void contains() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").contains("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").contains("name", "李").getMaps()
        );
    }
    //中间不一致------------------------------------
    @Test
    public void notLike() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").notLike("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").notLike("name", "李").getMaps()
        );
    }
    //左匹配------------------------------------
    @Test
    public void likeLeft() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").likeLeft("name", "四")
        );

        System.out.println(
                SQL.SELECT("student").column("name,age").likeLeft("name", "四").getMaps()
        );
    }
    @Test
    public void endsWith() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").endsWith("name", "四")
        );

        System.out.println(
                SQL.SELECT("student").column("name,age").endsWith("name", "四").getMaps()
        );
    }
    //右匹配------------------------------------
    @Test
    public void likeRight() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").likeRight("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").likeRight("name", "李").getMaps()
        );
    }
    @Test
    public void startsWith() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").startsWith("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").startsWith("name", "李").getMaps()
        );
    }
    @Test
    public void notStartsWith() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,age").notStartsWith("name", "李")
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").notStartsWith("name", "李").getMaps()
        );
    }
    //占位一致------------------------------------
    @Test
    public void like_() throws SQLException {

        System.out.println(
                SQL.SELECT("student").column("name,age").like_("name", "李_").getMaps()
        );
    }
}

package io.github.ldhai99.easyOrm.test.dbutils;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CloneTest {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DbTools.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    @Test
    public void getCount() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18)
                        .clone().setColumn("count(*) count").getMaps()
        );
        System.out.println(
                new SQL(con).select("student").column("name,age").eq("age", 18).getCount()

        );
    }

    @Test
    public void getCountGroup() throws SQLException {

        SQL sql = new SQL(con).select("student").column("sex")
                .count("count")//计数
                .min("age", "min")//最小
                .max("age", "max")//最大
                .avg("age", "avg")//平均
                .groupBy("sex")
                //.having(SQL.WHERE().gte("avg(age)",18))
                ;

        System.out.println(
                sql.getMaps()
        );
        System.out.println(
                sql.getCount()
        );
    }

    @Test
    public void page() throws SQLException {


        SQL sql = new SQL(con).select("student").column("name,age").orderBy("id");


        //获取记录个数
        System.out.println(
                sql.getCount()
        );

        //最慢--无需额外条件，最通用
        System.out.println(
                sql.clone().last(" limit 2,2").getMaps()

        );

        System.out.println(
                sql.clone().last("limit :start, :records")
                        .setParameter$("start", 2)
                        .setParameter$("records", 2)

        );
        System.out.println(
                sql.clone().last("limit :start ,:records")
                        .setParameter$("start", 2)
                        .setParameter$("records", 2).getMaps()

        );

        //有id，选记录时候，只选出id,最后再选出全部
        System.out.println(
                sql.clone().setWhere("").in("id",
                        SQL.SELECT(sql.clone().setColumn("id").last(" limit 2,2"), "a")
                                .column("id"))
        );
        System.out.println(
                sql.clone().setWhere("").in("id",
                        SQL.SELECT(sql.clone().setColumn("id").last(" limit 2,2"), "a")
                                .column("id")).getMaps()
        );


        //有id，按照id升序,有上一页的最后一行id,最快
        System.out.println(
                sql.clone().gt("id", 2).last(" limit 2")
        );
        System.out.println(
                sql.clone().gt("id", 2).last(" limit 2").getMaps()
        );
    }

    @Test
    public void pageModel() throws SQLException {


        SQL sql = new SQL(con).select("student").column("id,name").orderBy("id");

        System.out.println(
                PAGE.of(new PageModel().setCurrent(3).setSize(2),sql)
                        .pageMaps());

    }
}

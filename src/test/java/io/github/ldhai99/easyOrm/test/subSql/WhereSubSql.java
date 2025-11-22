package io.github.ldhai99.easyOrm.test.subSql;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.datasource.DefaultDataSourceProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WhereSubSql {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DefaultDataSourceProvider.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }
    @Test
    public void whereSubSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age").where(
                        SQL.ADDSQL("age = round(?)", 18.3)
                ).getMaps()
        );
        System.out.println(
                new SQL(con).select(" student ").column("name,age").where(
                        SQL.ADDSQL("age = round(:age)").setParameter("age", 18.3)
                ).getMaps()
        );
    }
    @Test
    public void eqSubSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .eq("age", SQL.ADDSQL("round(?)", 18.3)
                        ).getMaps()
        );
        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .eq("age", SQL.ADDSQL("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .eq("age"
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );


    }
    @Test
    public void nameSubSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .eq(SQL.ADDSQL("round(age,:arg0)").setParameter("arg0", 2)
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );
    }

    @Test
    public void likeSubSql() throws SQLException {


        System.out.println(
                new SQL(con).select("student").column("name,sex")
                        .like("sex", SQL.ADDSQL(" CONCAT('%',right(?,1),'%')", "男女")).getMaps()
        );
        System.out.println(
                new SQL(con).select("student").column("name,sex")
                        .like("sex", SQL.ADDSQL(" CONCAT('%',left(:sex,1),'%')").setParameter("sex", "男女")).getMaps()
        );
        System.out.println(
                new SQL(con).select("student").column("name,sex")
                        .like("sex", SQL.SELECT("dual").column(" CONCAT('%',left(:sex,1),'%')").setParameter("sex", "男女")).getMaps()
        );
    }

    @Test
    public void betweenSubSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSQL("round(?)", 18.3)
                                , SQL.ADDSQL("round(?)", 20.3)
                        ).getMaps()
        );
        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSQL("round(:age)").setParameter("age", 18.3)
                                , SQL.ADDSQL("round(:age)").setParameter("age", 20.3)
                        ).getMaps()
        );

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .between("age"
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 20.3)
                        ).getMaps()
        );


    }
    //in子查询
    @Test
    public void inSubSelect() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .in("age", SQL.SELECT("student").column("age").in("name", "张三", "李四"))
                        .getMaps()
        );
    }

    @Test
    public void notinselect() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("name,age")
                        .notIn("age", SQL.SELECT("student").column("age").in("name", "张三", "李四"))
                        .getMaps()
        );
    }




    //from子查询扩展------------------------

    @Test
    public void fromSubSql() throws SQLException {

        System.out.println(
                new SQL(con).column("name,sex")
                        .from(
                                SQL.SELECT("student").column("*"), "a")
                        .eq("age", 18).getMaps()
        );
    }

    @Test
    public void isSubSql() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age")
                        .isNotNull( SQL.SELECT("student1").column("age").eq("name","李四")
                        ).eq("name","李四").getMaps()
        );
    }


    //比较谓词支持子查询
    @Test
    public void setFunc() throws SQLException {

        System.out.println(
                new SQL(con).select(" student ").column("name,age").where(
                        SQL.ADDSQL("age = :age").
                                setParameter("age", SQL.ADDSQL("round(:age)").
                                        setParameter("age", 18.3))
                ).getMaps()
        );
    }





}

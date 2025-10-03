package io.github.ldhai99.easyOrm.test.addsql;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class FRAGMENTWhere {
    @Test
    public void whereSubSql() throws SQLException {

        System.out.println(
                 SQL.SELECT(" student ").column("name,age").where(
                        SQL.ADDSQL("age = round(?)", 18.3)
                ).getMaps()
        );
        System.out.println(
                SQL.SELECT(" student ").column("name,age").where(
                        SQL.ADDSQL("age = round(:age)").setParameter("age", 18.3)
                ).getMaps()
        );
    }
    @Test
    public void eqSubSql() throws SQLException {

        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .eq("age", SQL.ADDSQL("round(?)", 18.3)
                        ).getMaps()
        );
        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .eq("age", SQL.ADDSQL("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );

        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .eq("age"
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );


    }
    @Test
    public void nameSubSql() throws SQLException {

        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .eq(SQL.ADDSQL("round(age,:arg0)").setParameter("arg0", 2)
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                        ).getMaps()
        );
    }

    @Test
    public void likeSubSql() throws SQLException {


        System.out.println(
                SQL.SELECT("student").column("name,sex")
                        .like("sex", SQL.ADDSQL(" CONCAT('%',right(?,1),'%')", "男女")).getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,sex")
                        .like("sex", SQL.ADDSQL(" CONCAT('%',left(:sex,1),'%')").setParameter("sex", "男女")).getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,sex")
                        .like("sex", SQL.SELECT("dual").column(" CONCAT('%',left(:sex,1),'%')").setParameter("sex", "男女")).getMaps()
        );
    }

    @Test
    public void betweenSubSql() throws SQLException {

        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSQL("round(?)", 18.3)
                                , SQL.ADDSQL("round(?)", 20.3)
                        ).getMaps()
        );
        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .between("age"
                                , SQL.ADDSQL("round(:age)").setParameter("age", 18.3)
                                , SQL.ADDSQL("round(:age)").setParameter("age", 20.3)
                        ).getMaps()
        );

        System.out.println(
                SQL.SELECT(" student ").column("name,age")
                        .between("age"
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 18.3)
                                , SQL.SELECT("dual").column("round(:age)").setParameter("age", 20.3)
                        ).getMaps()
        );


    }
    //比较谓词支持子查询
    @Test
    public void setFunc() throws SQLException {

        System.out.println(
                SQL.SELECT(" student ").column("name,age").where(
                        SQL.ADDSQL("age = :age").
                                setParameter("age", SQL.ADDSQL("round(:age)").setParameter("age", 18.3))
                ).getMaps()
        );
    }

}

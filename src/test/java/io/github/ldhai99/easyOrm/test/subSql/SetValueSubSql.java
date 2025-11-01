package io.github.ldhai99.easyOrm.test.subSql;


import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.config.EasyOrmConfig;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class SetValueSubSql {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = EasyOrmConfig.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }

    //having加入参数
    @Test
    public void havingPara() throws SQLException {

        System.out.println(
                new SQL(con).select("student").column("sex")
                        .count("count")//计数
                        .min("age", "min")//最小
                        .max("age", "max")//最大
                        .avg("age", "avg")//平均
                        .groupBy("sex")
                        .having("avg(age)>=:age").setParameter("age", 18)
                        .getMaps()
        );
    }


}

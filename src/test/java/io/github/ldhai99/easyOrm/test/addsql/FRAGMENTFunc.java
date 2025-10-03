package io.github.ldhai99.easyOrm.test.addsql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.dynamic.DynamicSQL;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class FRAGMENTFunc {
    @Test
    public void funcTest() throws SQLException {
        System.out.println(
                SQL.SELECT("student").column("name,create_time")
                        .where(this.eqMonth("create_time","2022-12-01")).getMaps()
        );
    }
    public DynamicSQL eqMonth(String name, String value){
        return  SQL.ADDSQL("TIMESTAMPDIFF(MONTH,:name,:value)=0")
                .setParameter$("name",name)
                .setParameter("value",value) ;
    }
}

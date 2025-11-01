package io.github.ldhai99.easyOrm.test;

import io.github.ldhai99.easyOrm.SQL;

import io.github.ldhai99.easyOrm.config.EasyOrmConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
public class TestPassword {

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

    //获取单个值------------------------------------
    @Test
    public void updatePassword() throws SQLException {

        System.out.println(
                new SQL(con).select("s_user").where("username","admin").getMaps()
        );
        String salt= new SQL(con).select("s_user")
                .column("salt")
                .where("username","admin")
                .getString();
        String password=DigestUtils.md5Hex(String.format("hsweb.%s.framework.%s", "admin", salt));
        new SQL(con).update("s_user")
                .set("password",password)
                .where("username","admin")
                .update();


    }
}

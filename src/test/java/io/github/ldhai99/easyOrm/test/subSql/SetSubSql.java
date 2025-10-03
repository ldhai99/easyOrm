package io.github.ldhai99.easyOrm.test.subSql;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SetSubSql {
    private DataSource ds;
    private Connection con;

    @BeforeEach
    public void beforeTest() throws SQLException {
        con = DataSourceManager.getConnection();
    }

    @AfterEach
    public void afterTest() throws SQLException {
        con.close();
    }


}

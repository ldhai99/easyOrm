package io.github.ldhai99.easyOrm.autoConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;


@ConfigurationProperties(prefix="easyorm",ignoreInvalidFields = true)
public class EasyOrmProperties {
    private String database="mysql";
    //全局数据源
    public static DataSource ds=null;



    public void setDatabase(String database) {
        this.database = database;
    }
    public String getDatabase() {
        return database;
    }


    public static DataSource getDs() {
        return ds;
    }

    public static void setDs(DataSource ds) {
        EasyOrmProperties.ds = ds;
    }







}

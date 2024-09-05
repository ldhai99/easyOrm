package io.github.ldhai99.easyOrm.autoConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix="easyorm",ignoreInvalidFields = true)
public class EasyOrmProperties {
    private String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}

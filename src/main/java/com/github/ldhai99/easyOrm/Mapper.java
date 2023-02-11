package com.github.ldhai99.easyOrm;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.sql.SQLException;

public class Mapper {
    private NamedParameterJdbcTemplate template;
    SQL sql;
    public Mapper() {
    }

    public Mapper(NamedParameterJdbcTemplate template) {
        this.template = template;
        sql=new SQL(template);
    }

    public int insert(Object bean) throws IllegalAccessException, SQLException {
        Class clazz=bean.getClass();
        Field[] fields=clazz.getDeclaredFields();
        sql.insert(DbConfig.camel2under(clazz.getSimpleName()));
        for (int i=0;i<fields.length;i++){
            Field field=fields[i];
            field.setAccessible(true);
            Object value=field.get(bean);
            String name=DbConfig.camel2under(field.getName());
            if(value!=null)
            sql.set(name,value);
        }

        return sql.execute();

    }
}

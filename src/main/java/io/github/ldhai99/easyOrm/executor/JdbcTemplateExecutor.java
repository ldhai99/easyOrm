package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.JdbcModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JdbcTemplateExecutor implements IExecutor {
    //执行Sql----------------------------------------------
    //写数据库
    private NamedParameterJdbcTemplate template;



    private JdbcModel jdbcModel;

    JdbcTemplateExecutor(){

    }
    public JdbcTemplateExecutor(NamedParameterJdbcTemplate template){
        this.jdbcModel = jdbcModel;

    }

    public JdbcModel getJdbcDataModel() {
        return jdbcModel;
    }

    public IExecutor setJdbcDataModel(JdbcModel jdbcModel) {
        this.jdbcModel = jdbcModel;
        return  this;
    }
    //执行增加，删除，修改，返回记录个数

    public int update() {
        return template.update(jdbcModel.getSql(), jdbcModel.getParameterMap());
    }

    //插入，返回主键
    public Number insert() {
        return template.update(jdbcModel.getSql(), jdbcModel.getParameterMap());
    }
    //执行存储过程及ddl
    public int execute() {
        return template.update(jdbcModel.getSql(), jdbcModel.getParameterMap());
    }


    //查询数据库---------------------------------------------------------

    //返回单列单行数据

    public String string () {
        return   value(String.class);
    }
    public Number number() {
        return   value(Number.class);
    }

    public BigDecimal bigDecimal() {
        return   value(BigDecimal.class);
    }

    public Date date () {
        return   value(Date.class);
    }

    public < T > T value ( Class<T> requiredType) {
        return   template.queryForObject(jdbcModel.getSql(), jdbcModel.getParameterMap(),requiredType);
    }
    //返回单列list数据
    public List<String> strings () {
        return   values(String.class);
    }
    public  List<Number> numbers () {
        return   values(Number.class);
    }

    public  List<BigDecimal> bigDecimals () {
        return   values(BigDecimal.class);
    }

    public  List<Date> dates () {
        return   values(Date.class);
    }
    public < T > List<T> values ( Class<T> requiredType) {
        return   template.queryForList(jdbcModel.getSql(), jdbcModel.getParameterMap(),requiredType);
    }
    //返回单行数据

    public Map<String, Object> map() {
        return template.queryForMap(jdbcModel.getSql(), jdbcModel.getParameterMap());
    }
    //返回多行数据
    public List<Map<String, Object>> maps() {

       return template.queryForList(jdbcModel.getSql(), jdbcModel.getParameterMap());

    }

    //返回Bean实体
    public <T> T bean(Class<T> T) {
        return template.queryForObject(jdbcModel.getSql(), jdbcModel.getParameterMap(), new BeanPropertyRowMapper<T>(T));
    }

    //返回Bean list
    public <T> List<T> beans(Class<T> T) {
        return template.query(jdbcModel.getSql(), jdbcModel.getParameterMap(), new BeanPropertyRowMapper<T>(T));
    }
}

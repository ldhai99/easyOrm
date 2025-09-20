package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.builder.ExecutorHandler;
import io.github.ldhai99.easyOrm.dao.orm.DatabaseResultMapper;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class JdbcTemplateExecutor extends AbstractExecutor {

    private NamedParameterJdbcTemplate template;


    // 移除无参构造方法，或者改为从 DataSourceManager 获取默认数据源
    public JdbcTemplateExecutor() {
        this(DataSourceManager.getDefaultDataSource());
    }

    public JdbcTemplateExecutor(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public JdbcTemplateExecutor(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    //执行Sql----------------------------------------------
    //写数据库
//更新数据库----------------------------------------------------------------------------------------------------
     //NamedParameterJdbcTemplate 是 Spring JDBC 框架中的一个高级工具，它简化了数据库操作，特别是当你需要执行带有命名参数的 GetHandler 语句时。
    // 与 JdbcTemplate 相比，NamedParameterJdbcTemplate 允许你使用更具可读性的命名参数而不是传统的问号（?）占位符。
    // 并通过 Map<String, Object>  来传递参数，然后调用相应的方法来获取结果：
    //更新
    public int update(ExecutorHandler sql) {
        return   template.update( sql.toString(),sql.getParameterMap());
    }
    public Number insert(ExecutorHandler sql) {
        return   template.update( sql.toString(),sql.getParameterMap());
    }
    public int delete(ExecutorHandler sql) {
        return   template.update( sql.toString(),sql.getParameterMap());
    }
    public int execute(ExecutorHandler sql) {
        return   template.update( sql.toString(),sql.getParameterMap());
    }
    //返回单列单行数据---------------------

    public < T > T getValue (ExecutorHandler sql, Class<T> requiredType) {
        try {
            Object value = template.queryForObject(sql.toString(), sql.getParameterMap(), requiredType);
            return (T) value;
        }catch (IncorrectResultSizeDataAccessException e){
            List mapList=getValues(sql, requiredType);
            if(mapList.size()>=1)
                return (T) mapList.get(0);
            else
                return null;
        }

    }
    //返回单列list数据

    public < T > List<T> getValues (ExecutorHandler sql, Class<T> requiredType) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(),requiredType);
    }

    //返回单行map数据-----------------------------
    public Map<String, Object>  getMap(ExecutorHandler sql){

        try {
            return template.queryForMap(sql.toString(), sql.getParameterMap());
        }catch (IncorrectResultSizeDataAccessException e){

            List<Map<String,Object>> mapList=getMaps(sql);
            if(mapList.size()>=1)
                return mapList.get(0);
            else
                return null;
        }
    }

    //返回多行map数据
    public List<Map<String,Object>> getMaps(ExecutorHandler sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap());
    }
    //返回Bean实体
    public <T> T getBean(ExecutorHandler sql, Class<T> T)  {

        try {
            Object value = template.queryForObject(sql.toString(), sql.getParameterMap(), new BeanPropertyRowMapper<T>(T));
            return (T) value;

        }catch (IncorrectResultSizeDataAccessException e){

            List<T> mapList=getBeans(sql,T);
            if(mapList.size()>=1)
                return mapList.get(0);
            else
                return null;
        }
    }

    public <T> List<T> getBeans(ExecutorHandler sql, Class<T> clazz)  {

        //return template.query(sql.toString(), sql.getParameterMap(), new BeanPropertyRowMapper<T>(clazz));
// 先查询 Map 列表
        List<Map<String, Object>> mapList = template.queryForList(sql.toString(), sql.getParameterMap());

        // ✅ 通过工具方法转换为 Java Bean 列表，内部使用 MappingResolver
        return DatabaseResultMapper.mapRowsToBeans(mapList, clazz);
    }
    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
}

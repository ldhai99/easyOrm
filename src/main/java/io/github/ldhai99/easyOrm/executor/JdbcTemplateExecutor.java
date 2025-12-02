package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.builder.ExecutorHandler;
import io.github.ldhai99.easyOrm.context.DbType;
import io.github.ldhai99.easyOrm.dao.orm.DatabaseResultMapper;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class JdbcTemplateExecutor extends AbstractExecutor {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private DbType dbType;
    private DataSource dataSource;
    private boolean dbTypeDetected = false;

    // 移除无参构造方法，或者改为从 DataSourceManager 获取默认数据源
    public JdbcTemplateExecutor(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = jdbcTemplate.getJdbcTemplate().getDataSource();
    }

    @Override
    public DbType getDbType() {
        if (!dbTypeDetected && dataSource != null) {
            // 懒加载检测
            this.dbType = DbType.fromDataSource(dataSource);
            this.dbTypeDetected = true;
        }
        return dbType != null ? dbType : DbType.OTHER;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
    // 设置数据库类型（可手动指定）
    public void setDbType(DbType dbType) {
        this.dbType = dbType;
        this.dbTypeDetected = true;
    }
    //执行Sql----------------------------------------------
    //写数据库
//更新数据库----------------------------------------------------------------------------------------------------
     //NamedParameterJdbcTemplate 是 Spring JDBC 框架中的一个高级工具，它简化了数据库操作，特别是当你需要执行带有命名参数的 GetHandler 语句时。
    // 与 JdbcTemplate 相比，NamedParameterJdbcTemplate 允许你使用更具可读性的命名参数而不是传统的问号（?）占位符。
    // 并通过 Map<String, Object>  来传递参数，然后调用相应的方法来获取结果：
    //更新
    public int update(ExecutorHandler sql) {
        return   jdbcTemplate.update( sql.toString(),sql.getParameterMap());
    }
    public Number insert(ExecutorHandler sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
           jdbcTemplate.update( sql.toString(), (SqlParameterSource)(new MapSqlParameterSource(sql.getParameterMap())),keyHolder);
        return extractPrimaryKey(keyHolder);
    }
    /**
     * 智能提取主键值
     * - 单个值：直接返回 getKey()
     * - 多个值：查找 id/ID 列的值
     */
    private Number extractPrimaryKey(KeyHolder keyHolder) {
        try {
            // 先尝试直接获取单个主键
            return keyHolder.getKey();
        } catch (InvalidDataAccessApiUsageException e) {
            // 如果是多值错误，从键映射中提取
            return extractKeyFromMultiple(keyHolder);
        }
    }

    /**
     * 从多个返回值中提取主键
     */
    private Number extractKeyFromMultiple(KeyHolder keyHolder) {
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys == null || keys.isEmpty()) {
            return null;
        }

        System.out.println("🔍 检测到多个返回值: " + keys);

        // 优先查找 id 列（不区分大小写）
        for (String key : keys.keySet()) {
            if ("id".equalsIgnoreCase(key)) {
                Object value = keys.get(key);
                if (value instanceof Number) {
                    System.out.println("✅ 找到主键 ID: " + value);
                    return (Number) value;
                }
            }
        }

        // 如果没有找到 id 列，尝试返回第一个数值类型的值
        for (Object value : keys.values()) {
            if (value instanceof Number) {
                System.out.println("⚠️  使用第一个数值作为主键: " + value);
                return (Number) value;
            }
        }

        System.out.println("❌ 未找到合适的主键值");
        return null;
    }

    public int delete(ExecutorHandler sql) {
        return   jdbcTemplate.update( sql.toString(),sql.getParameterMap());
    }
    public int execute(ExecutorHandler sql) {
        return   jdbcTemplate.update( sql.toString(),sql.getParameterMap());
    }
    //返回单列单行数据---------------------

    public < T > T getValue (ExecutorHandler sql, Class<T> requiredType) {
        try {
            Object value = jdbcTemplate.queryForObject(sql.toString(), sql.getParameterMap(), requiredType);
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
        return   jdbcTemplate.queryForList(sql.toString(), sql.getParameterMap(),requiredType);
    }

    //返回单行map数据-----------------------------
    public Map<String, Object>  getMap(ExecutorHandler sql){

        try {
            return jdbcTemplate.queryForMap(sql.toString(), sql.getParameterMap());
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
        return   jdbcTemplate.queryForList(sql.toString(), sql.getParameterMap());
    }
    //返回Bean实体
    public <T> T getBean(ExecutorHandler sql, Class<T> T)  {

        try {
            Object value = jdbcTemplate.queryForObject(sql.toString(), sql.getParameterMap(), new BeanPropertyRowMapper<T>(T));
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

        //return jdbcTemplate.query(sql.toString(), sql.getParameterMap(), new BeanPropertyRowMapper<T>(clazz));
// 先查询 Map 列表
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString(), sql.getParameterMap());

        // ✅ 通过工具方法转换为 Java Bean 列表，内部使用 MappingResolver
        return DatabaseResultMapper.mapRowsToBeans(mapList, clazz);
    }
    public NamedParameterJdbcTemplate getTemplate() {
        return jdbcTemplate;
    }

    public void setTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

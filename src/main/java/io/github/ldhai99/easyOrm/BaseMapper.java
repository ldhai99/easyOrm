package io.github.ldhai99.easyOrm;


import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class BaseMapper {
    //@Autowired
    private NamedParameterJdbcTemplate template;

    public BaseMapper(){

    }


    //返回单列单行数据
    public String oneString (SQL sql) {
        return   template.queryForObject(sql.toString(), sql.getParameterMap(),String.class);
    }

    public Long oneLong (SQL sql) {
        return   template.queryForObject(sql.toString(), sql.getParameterMap(),Long.class);
    }

    public BigDecimal oneDecimal (SQL sql) {
        return   template.queryForObject(sql.toString(), sql.getParameterMap(), BigDecimal.class);
    }
    public Date oneDate (SQL sql) {
        return   template.queryForObject(sql.toString(), sql.getParameterMap(), Date.class);
    }
    public < T > T one (SQL sql, Class<T> requiredType) {
        return   template.queryForObject(sql.toString(), sql.getParameterMap(),requiredType);
    }
    //返回单列list数据
    public  List<String> oneStrings (SQL sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(),String.class);
    }

    public  List<Long> oneLongs (SQL sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(),Long.class);
    }

    public  List<BigDecimal> oneDecimals (SQL sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(), BigDecimal.class);
    }
    public  List<Date> oneDates (SQL sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(), Date.class);
    }
    public < T > List<T> ones (SQL sql, Class<T> requiredType) {
        return   template.queryForList(sql.toString(), sql.getParameterMap(),requiredType);
    }

    //返回单行map数据
    public Map map(SQL sql){

          try {
               return template.queryForMap(sql.toString(), sql.getParameterMap());
          }catch (IncorrectResultSizeDataAccessException e){

              List<Map<String,Object>> mapList=maps(sql);
              if(mapList.size()>=1)
                  return mapList.get(0);
              else
                  return null;
          }
    }

    //返回多行map数据
    public List<Map<String,Object>> maps(SQL sql) {
        return   template.queryForList(sql.toString(), sql.getParameterMap());
    }
    //更新
    public int update(SQL sql) {
        return   template.update( sql.toUpdate(),sql.getParameterMap());
    }
    public int insert(SQL sql) {
        return   template.update( sql.toInsert(),sql.getParameterMap());
    }
    public int delete(SQL sql) {
        return   template.update( sql.toDelete(),sql.getParameterMap());
    }



}

package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.page.MysqlPageSqlByStartId;
import io.github.ldhai99.easyOrm.page.PAGE;
import io.github.ldhai99.easyOrm.tools.SnowflakeId;
import io.github.ldhai99.easyOrm.datamodel.BaseDm;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;


import java.io.Serializable;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends BaseDm> {
    public T dm;


    public BaseDao() {
        if (!hasArgConstructor()) {
            //System.out.println("Parent constructor");
            Class<T> dmClass = getGenericParameterClass();
            try {
                dm = dmClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //判断是否有构造方法
    public boolean hasArgConstructor() {
        // 获取当前子类的Class对象
        Class<? extends BaseDao<T>> currentClass = (Class<? extends BaseDao<T>>) this.getClass();

        // 检查子类是否有构造方法
        Constructor<?>[] constructors = currentClass.getDeclaredConstructors();
        //超过两个肯定有无参构造方法
        if (constructors.length >= 2)
            return true;

        //只有一个构造方法时候,判断是否有注解,有注解就是手工编写的，没有注解认为是默认的构造方法
        Constructor<?> constructor = constructors[0];
        // 检查构造方法是否有显式的修饰符
        if (constructor.getParameterCount() == 0 && constructor.isAnnotationPresent(ExplicitConstructor.class)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericParameterClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

    public BaseDao(T dm) {
        this.dm = dm;
    }

    public T getDm() {
        return dm;
    }

    public void setDm(T dm) {
        this.dm = dm;
    }

    //增加---------------------
    public int insert(Map<String, Object> data) {

        if (data.containsKey(dm.table_id) && data.get(dm.table_id) != null)

            return SQL.INSERT(dm.update_table).
                    setMap(data).

                    update();
        else
            return SQL.INSERT(dm.update_table).
                    setMap(dm.update_fields, data).
                    set(dm.table_id, SnowflakeId.getId()).
                    update();
    }

    //删除---------------------
    //通过id删除
    public int deleteById(Serializable id) {

        return SQL.DELETE(dm.update_table).
                eq(dm.table_id, id).
                update();
    }

    //通过id列表删除一组
    public int deleteByIds(ArrayList ids) {

        return SQL.DELETE(dm.update_table).
                in(dm.table_id, ids).
                update();
    }

    //通过id数组删除一组
    public int deleteByIds(Object... ids) {

        return SQL.DELETE(dm.update_table).
                in(dm.table_id, ids).
                update();
    }

    //通过条件组删除
    public int deleteByMap(Map<String, Object> columnMap) {
        return SQL.DELETE(dm.update_table).
                eqMap(columnMap).
                update();
    }

    //通过条件构造器删除
    public int deleteByWhere(SQL sql) {
        return SQL.DELETE(dm.update_table).
                where(sql).
                update();
    }

    //修改--------------------------------
    //通过id修改，用配置的修改字段
    public int updateById(Map<String, Object> data) {

        return SQL.UPDATE(dm.update_table).
                setMap(dm.update_fields, data).
                set(dm.table_id, data.get(dm.table_id)).
                update();

    }

    //通过id修改，传入修改字段
    public int updateById(String fields, Map<String, Object> data) {


        return SQL.UPDATE(dm.update_table)
                .setMap(fields, data)
                .set(dm.table_id, data.get(dm.table_id))
                .update();

    }

    //通过map条件修改，用配置的修改字段

    public int updateByMap(Map<String, Object> data, Map<String, Object> columnMap) {
        return SQL.UPDATE(dm.update_table).
                setMap(dm.update_fields, data).
                eqMap(columnMap).
                update();
    }

    //通过map条件修改，传入修改字段
    public int updateByMap(String fields, Map<String, Object> data, Map<String, Object> columnMap) {
        return SQL.UPDATE(dm.update_table).
                setMap(fields, data).
                eqMap(columnMap).
                update();
    }


    //查询---------------------------------------
    //通过id，获取一条记录为map
    public Map<String, Object> getMapById(Serializable id) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eq(dm.table_id, id)
                .getMap();
    }

    public <E> E getBeanById(Serializable id, Class<E> E) {
        return (E)SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eq(dm.table_id, id)
                .getBean(E);
    }

    //通过一组id列表，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(ArrayList ids) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getMaps();
    }

    public <E> List<E> getBeansByIds(ArrayList ids, Class<E> E) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getBeans(E);
    }

    //通过一组id数组，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(Object... ids) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getMaps();
    }

    public <E> List<E> getBeansByIds(Class<E> E, Serializable... ids) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getBeans(E);
    }

    //通过map条件，获取一条记录为map
    public Map<String, Object> getMapByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getMap();
    }

    public <E> E getBeanByMap(Map<String, Object> columnMap, Class<E> E) {
        return (E)SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getBean(E);
    }

    //通过map条件，获取多条记录为maps
    public List<Map<String, Object>> getMapsByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getMaps();
    }

    public <E> List<E> getBeansByMap(Map<String, Object> columnMap, Class<E> E) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getBeans(E);
    }

    //通过条件构造器，获取多条记录为maps
    public List<Map<String, Object>> getMapsByWhere(SQL sql) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .where(sql)
                .getMaps();
    }

    public <E> List<E> getBeansByWhere(SQL sql, Class<E> E) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .where(sql)
                .getBeans(E);
    }

    //存在--------------
    //通过id判断存在
    public boolean exists(String id) {
        Map map = SQL.SELECT(dm.select_table)
                .eq(dm.table_id, id)
                .getMap();
        if (map == null || map.isEmpty()) {
            return false;
        }
        return true;

    }

    //通过map判断存在
    public boolean existsByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .eqMap(columnMap)
                .isExists();
    }


    //获取数量------------------------
    //通过map条件计数
    public Long getCountByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .eqMap(columnMap)
                .getCount().longValue();
    }

    //通过name-value计数
    public Long getCountByField(String name, Object value) {
        return SQL.SELECT(dm.select_table)
                .eq(name, value)
                .getCount().longValue();
    }

    //通过where构造器计数
    public Long getCountByWhere(SQL sql) {
        return SQL.SELECT(dm.select_table)
                .where(sql)
                .getCount().longValue();
    }

    //获取页面数据
    public List<Map<String, Object>> getPageBySQL(PageModel pageModel, SQL sql) {
        return PAGE.of(pageModel, sql).getPageMaps();


    }

    //page传入翻页条件，传入条件构造器，普通的翻页
    public List<Map<String, Object>> getPageByWhere(PageModel pageModel, SQL sqlWhere) {
        return PAGE.of(pageModel).setSql(
                        SQL.SELECT(dm.select_table)
                                .column(dm.select_fields)
                                .where(sqlWhere))
                .getPageMaps();

    }

    //   page传入翻页条件，传入条件构造器，翻页条件要加上页的起始行id
    public List<Map<String, Object>> getPageByStartId(PageModel pageModel, SQL sql) {
        return PAGE.of(pageModel).setSql(sql)
                .setPageSqlGenerator(new MysqlPageSqlByStartId())
                .getPageMaps();

    }


}

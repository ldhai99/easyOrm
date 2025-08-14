package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.dao.orm.EntityValueMapper;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSqlByStartId;
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

    //增加------------------------------------------------------------------------------------
    // 在 SQL 类中添加以下方法

    /**
     * 插入实体对象
     * @param <T> 实体类型
     * @param entity 要插入的实体对象
     * @return 影响的行数
     */
    public <T> int insert(T entity) {
        return insert(entity, false);
    }

    /**
     * 插入实体对象（可选是否忽略空值）
     *@param <T> 泛型类型，表示实体类的类型
     * @param entity     实体对象
     * @param ignoreNull 是否忽略空值字段
     * @return 当前SQL实例
     */
    public <T> int insert(T entity, boolean ignoreNull) {
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, ignoreNull);
        return insert(fieldMap);
    }

    public boolean saveOrUpdate(Object entity) {
        // 获取实体 ID（假设 ID 是 Serializable 类型）
        Serializable id = getEntityId(entity);
        if (id == null || id.equals(0L)) {
            // ID 不存在，执行插入
            return insert(entity) > 0 ? true : false;
        } else {
            // ID 存在，执行更新
            return update(entity) > 0 ? true : false;
        }
    }

    public int insert(Map<String, Object> data) {

        if (data.containsKey(dm.tableId) && data.get(dm.tableId) != null)

            return SQL.INSERT(dm.updateTable).
                    setMap(data).

                    update();
        else
            return SQL.INSERT(dm.updateTable).
                    setMap(dm.updateFields, data).
                    set(dm.tableId, SnowflakeId.getId()).
                    update();
    }

    //删除------------------------------------------------------------------------------------------
    //通过id删除
    public int deleteById(Serializable id) {

        return SQL.DELETE(dm.updateTable).
                eq(dm.tableId, id).
                update();
    }

    //通过id列表删除一组
    public int deleteByIds(ArrayList ids) {

        return SQL.DELETE(dm.updateTable).
                in(dm.tableId, ids).
                update();
    }

    //通过id数组删除一组
    public int deleteByIds(Object... ids) {

        return SQL.DELETE(dm.updateTable).
                in(dm.tableId, ids).
                update();
    }

    //通过条件组删除
    public int delete(Map<String, Object> columnMap) {
        return this.deleteByMap(columnMap);
    }
    public int deleteByMap(Map<String, Object> columnMap) {
        return SQL.DELETE(dm.updateTable).
                eqMap(columnMap).
                update();
    }

    //通过条件构造器删除
    public int delete(SQL sql) {
        return this.deleteBySql(sql);
    }
    public int deleteBySql(SQL sql) {
        return SQL.DELETE(dm.updateTable).
                where(sql).
                update();
    }

    //修改------------------------------------------------------------------------------------------

    /**
     * 根据主键更新实体对象（自动识别主键字段）
     *@param <T> 泛型类型，表示实体类的类型
     * @param entity 实体对象
     * @return 当前SQL实例
     */
    public <T> int update(T entity) {
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, true);

        return this.updateById(fieldMap);

    }

    public <T> int update(T entity, boolean ignoreNull) {
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, ignoreNull);

        return this.updateById(fieldMap);

    }

    //通过id修改，用配置的修改字段
    public int updateById(Map<String, Object> data) {
        Object id = data.get(dm.tableId);

        return SQL.UPDATE(dm.updateTable).
                setMap(data).
                eq(dm.tableId, id).
                update();

    }

    //通过id修改，传入修改字段
    public int updateById(String fields, Map<String, Object> data) {


        return SQL.UPDATE(dm.updateTable)
                .setMap(fields, data)
                .eq(dm.tableId, data.get(dm.tableId))
                .update();

    }

    //通过map条件修改，用配置的修改字段

    public int updateByMap(Map<String, Object> data, Map<String, Object> whereMap) {
        return SQL.UPDATE(dm.updateTable).
                setMap(data).
                eqMap(whereMap).
                update();
    }

    //通过map条件修改，传入修改字段
    public int updateByMap(String fields, Map<String, Object> data, Map<String, Object> whereMap) {
        return SQL.UPDATE(dm.updateTable).
                setMap(fields, data).
                eqMap(whereMap).
                update();
    }

    //通过条件构造器修改
    public int updateBySql(Map map, SQL sql) {

        return SQL.UPDATE(dm.updateTable).
                setMap(map).
                where(sql).
                update();
    }

    //查询-------------------------------------------------------------------------------------------------------
    //通过id，获取一条记录为map
    public Map<String, Object> getMapById(Serializable id) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eq(dm.tableId, id)
                .getMap();
    }

    public <E> E getBeanById(Serializable id, Class<E> E) {
        return (E) SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eq(dm.tableId, id)
                .getBean(E);
    }

    //通过一组id列表，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(ArrayList ids) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .in(dm.tableId, ids)
                .getMaps();
    }

    public <E> List<E> getBeansByIdList(ArrayList ids, Class<E> E) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .in(dm.tableId, ids)
                .getBeans(E);
    }

    public <E> List<E> getBeansByIds(Class<E> E, Serializable... ids) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .in(dm.tableId, ids)
                .getBeans(E);
    }

    //通过一组id数组，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(Object... ids) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .in(dm.tableId, ids)
                .getMaps();
    }


    //通过map条件，获取一条记录为map
    public Map<String, Object> getMapByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eqMap(columnMap)
                .getMap();
    }

    public <E> E getBeanByMap(Map<String, Object> columnMap, Class<E> E) {
        return (E) SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eqMap(columnMap)
                .getBean(E);
    }

    //通过map条件，获取多条记录为maps
    public List<Map<String, Object>> getMapsByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eqMap(columnMap)
                .getMaps();
    }

    public <E> List<E> getBeansByMap(Map<String, Object> columnMap, Class<E> E) {
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .eqMap(columnMap)
                .getBeans(E);
    }

    public SQL upgradeToSelect(SQL sql) {
        if (sql.isSelect())
            return sql;
        if (sql.isNotOnlyWhere()) {
            sql.whereToSelect().select(dm.selectTable).column(dm.selectFields);
            return sql;
        }
        return SQL.SELECT(dm.selectTable)
                .column(dm.selectFields)
                .where(sql);
    }

    //通过条件构造器，获取多条记录为maps
    public <E> E getBeanBySql(SQL sql, Class<E> E) {
        return upgradeToSelect(sql).getBean(E);
    }

    public Map<String, Object> getMapBySql(SQL sql) {
        return upgradeToSelect(sql).getMap();

    }

    public List<Map<String, Object>> getMapsBySql(SQL sql) {
        return upgradeToSelect(sql).getMaps();

    }

    //通过条件构造器，获取多条记录为beans
    public <E> List<E> selectList(SQL sql, Class<E> E) {
        return getBeansBySql(sql, E);

    }
    public <E> List<E> getBeansBySql(SQL sql, Class<E> E) {
        return upgradeToSelect(sql).getBeans(E);

    }


    //判断实体是否存在
    public <T> Serializable getEntityId(T entity) {
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, true);
        String id_value = (String) fieldMap.get(dm.tableId);
        Map map = getMapById(id_value);
        return (Serializable) map.get(dm.tableId);
    }

    //存在--------------
    //通过id判断存在
    public boolean exists(String id) {
        Map map = SQL.SELECT(dm.selectTable)
                .eq(dm.tableId, id)
                .getMap();
        if (map == null || map.isEmpty()) {
            return false;
        }
        return true;

    }

    //通过map判断存在
    public boolean existsByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.selectTable)
                .eqMap(columnMap)
                .isExists();
    }

    public boolean existsBySql(SQL sql) {
        return upgradeToSelect(sql).isExists();

    }


    //获取数量------------------------
    //通过map条件计数
    public Long getCountByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.selectTable)
                .eqMap(columnMap)
                .getCount().longValue();
    }

    //通过name-value计数
    public Long getCountByField(String name, Object value) {
        return SQL.SELECT(dm.selectTable)
                .eq(name, value)
                .getCount().longValue();
    }

    //通过where构造器计数
    public Long getCountBySql(SQL sql) {
        return upgradeToSelect(sql).getCount().longValue();

    }

    //获取页面数据--------------------------------------

    //page传入翻页条件，传入条件构造器，普通的翻页
    public List<Map<String, Object>> pageMapsBySql(PageModel pageModel, SQL sql) {

        return PAGE.of(pageModel).setSql(upgradeToSelect(sql)).pageMaps();

    }

    public <E> List<E> pageBeansBySql(PageModel pageModel, SQL sql, Class<E> E) {

        return PAGE.of(pageModel).setSql(upgradeToSelect(sql)).pageBeans(E);

    }

    //   page传入翻页条件，传入条件构造器，翻页条件要加上页的起始行id
    public List<Map<String, Object>> pageByStartId(PageModel pageModel, SQL sql) {
        return PAGE.of(pageModel).setSql(sql)
                .setPageSqlGenerator(new MysqlPageSqlByStartId())
                .pageMaps();

    }


}

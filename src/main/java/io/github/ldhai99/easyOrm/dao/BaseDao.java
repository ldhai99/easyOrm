package io.github.ldhai99.easyOrm.dao;

import io.github.ldhai99.easyOrm.base.SnowflakeId;
import io.github.ldhai99.easyOrm.datamodel.BaseDm;
import io.github.ldhai99.easyOrm.page.MysqlPageData;
import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends BaseDm> {

    public T dm;

    public BaseDao() {

    }

    public BaseDao(T dm) {
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
    //通过一组id列表，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(ArrayList ids) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getMaps();
    }
    //通过一组id数组，获取多条记录为maps
    public List<Map<String, Object>> getMapsByIds(Object... ids) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .in(dm.table_id, ids)
                .getMaps();
    }
    //通过map条件，获取一条记录为map
    public Map<String, Object> getMapByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getMap();
    }
    //通过map条件，获取多条记录为maps
    public List<Map<String, Object>> getMapsByMap(Map<String, Object> columnMap) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .eqMap(columnMap)
                .getMaps();
    }
    //通过条件构造器，获取多条记录为maps
    public List<Map<String, Object>> getMapsByWhere(SQL sql) {
        return SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .where(sql)
                .getMaps();
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
    public PageModel getPageBySQL(PageModel page, SQL sql) {
        return page.setPageData(new MysqlPageData(sql) {
            public List<Map<String, Object>> getPageData()  {
                //具体哪一种可以翻页可以更换
                return getPageData(sql, page.getPageStartRow(), page.getPageRecorders());

            }

        }).execute();
    }
    //page传入翻页条件，传入条件构造器，普通的翻页
    public PageModel getPageByWhere(PageModel page, SQL sqlWhere) {
        return page.setPageData(new MysqlPageData(SQL.SELECT(dm.select_table)
                .column(dm.select_fields)
                .where(sqlWhere)) {
            public List<Map<String, Object>> getPageData() {

                //具体哪一种可以翻页可以更换
                return getPageData(sql , page.getPageStartRow(), page.getPageRecorders());
            }

        }).execute();
    }

    //   page传入翻页条件，传入条件构造器，翻页条件要加上页的起始行id

    public PageModel getPageByStartId(PageModel page, SQL sql) {
        return page.setPageData(new MysqlPageData(sql) {
            public List<Map<String, Object>> getPageData()  {
                //具体哪一种可以翻页可以更换
                return sql.clone().gt(page.getId(), page.getPageStartId())
                        .last(" limit :records")
                        .setValue$("records", page.getPageRecorders())
                        .getMaps();

            }

        }).execute();
    }


}

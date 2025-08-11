package io.github.ldhai99.easyOrm.service;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.dao.BaseDao;
import io.github.ldhai99.easyOrm.dao.orm.EntityValueMapper;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.tools.SqlTools;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public abstract class BaseService<T> {
    public abstract BaseDao getBaseDao();

    protected Class<T> entityClass;
    int DEFAULT_BATCH_SIZE = 1000;

    public BaseService() {
        // 获取当前类的泛型参数
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            this.entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("必须指定泛型类型");
        }
    }

    // 保存实体（插入）-----------------------------------------------
    @Transactional
    public boolean save(T entity) {
        if (getBaseDao().insert(entity) > 0) {
            return true;
        }
        return false;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        int count = 0;
        for (T entity : entityList) {
            if (getBaseDao().insert(entity) > 0) {
                count++;
            }
            if (count >= batchSize) {
                break;
            }
        }
        return count >= 0 ? true : false;

    }

    // 批量保存
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveBatch(List<T> entities) {
        return saveBatch(entities, DEFAULT_BATCH_SIZE);
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveOrUpdate(T entity) {
        // 获取实体 ID（假设 ID 是 Serializable 类型）
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, true);
        String id_value = (String) fieldMap.get(getBaseDao().getDm().tableId);
        Map map = getBaseDao().getMapById(id_value);
        if (map == null) {
            // ID 不存在，执行插入
            return getBaseDao().insert(entity) > 0 ? true : false;
        } else {
            // ID 存在，执行更新
            return getBaseDao().update(entity) > 0 ? true : false;
        }
    }

    ;

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        return this.saveOrUpdateBatch(entityList, 1000);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        int count = 0;
        for (T entity : entityList) {
            if (saveOrUpdate(entity)) {
                count++;
            }
            if (count >= batchSize) {
                break;
            }
        }
        return count >= 0 ? true : false;
    }


    // 删除实体（通过主键）-----------------------------------------
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeById(Serializable id) {
        return this.getBaseDao().deleteById(id) > 0 ? true : false;
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeById(Serializable id, boolean useFill) {
        throw new UnsupportedOperationException("不支持的方法!");
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeById(T entity) {
        // 获取实体 ID（假设 ID 是 Serializable 类型）
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, true);
        String id_value = (String) fieldMap.get(getBaseDao().getDm().tableId);

        return this.getBaseDao().deleteById(id_value) > 0 ? true : false;
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeByMap(Map<String, Object> columnMap) {
        // Assert.notEmpty(columnMap, "error: columnMap must not be empty", new Object[0]);
        return this.getBaseDao().deleteByMap(columnMap) > 0;
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean remove(SQL sqlWhere) {
        return this.getBaseDao().deleteBySql(sqlWhere) > 0 ? true : false;
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeByIds(Collection<?> list) {
        return SqlTools.isEmpty(list) ? false : this.getBaseDao().deleteByIds(list) > 0 ? true : false;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        if (SqlTools.isEmpty(list)) {
            return false;
        } else {
            return useFill ? this.removeBatchByIds(list, true) : this.getBaseDao().deleteByIds(list) > 0 ? true : false;
        }
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeBatchByIds(Collection<?> list) {
        return this.removeBatchByIds(list, 1000);
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return this.removeBatchByIds(list, 1000, useFill);
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeBatchByIds(Collection<?> list, int batchSize) {
        throw new UnsupportedOperationException("不支持的方法!");
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill) {
        throw new UnsupportedOperationException("不支持的方法!");
    }

    // 通过ID删除
    @Transactional
    public int deleteById(Serializable id) {
        return getBaseDao().deleteById(id);
    }

    // 批量删除通过ID列表
    @Transactional
    public int deleteByIds(Iterable<? extends Serializable> ids) {
        return getBaseDao().deleteByIds((ids));
    }

    // 通过条件删除
    @Transactional
    public int deleteByMap(Map<String, Object> columnMap) {
        return getBaseDao().deleteByMap(columnMap);
    }

    // 修改实体（通过主键）------------------------------------------------
    @Transactional
    public int update(T entity) {
        return getBaseDao().update(entity);
    }
    @Transactional
    public boolean updateById(T entity) {
        return this.getBaseDao().update(entity) > 0 ? true : false;
    }
    @Transactional
    public boolean update(SQL sqlWhere) {
        return this.update((T) null, sqlWhere);
    }
    @Transactional
    public boolean update(T entity, SQL sqlWhere) {
        Map<String, Object> fieldMap = EntityValueMapper.toColumnValues(entity, true);
        return this.getBaseDao().updateBySql(fieldMap, sqlWhere) > 0 ? true : false;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean updateBatchById(Collection<T> entityList) {
        return this.updateBatchById(entityList, 1000);
    }
    @Transactional
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        int count = 0;
        for (T entity : entityList) {
            if (this.updateById(entity)) {
                count++;
            }
            if (count >= batchSize) {
                break;
            }
        }
        return count >= 0 ? true : false;
    }

    //--查询部分------------------------------------------------
    // 通过sqlWhere条件查询----------------------------
    //-----查询单个记录----通过sql where-----

    public T getBeanBySql(SQL sqlWhere) {
        return (T) getBaseDao().getBeanBySql(sqlWhere, entityClass);
    }

    public Map<String, Object> getMapBySql(SQL sqlWhere) {
        return getBaseDao().getMapBySql(sqlWhere);
    }

    public Optional<T> getBeanOptBySql(SQL sqlWhere) {
        return Optional.ofNullable(getBeanBySql(sqlWhere));
    }


    //-----查询多个记录----通过sql where-----
    public List<T> getBeansBySql(SQL sql) {
        return this.getBaseDao().getBeansBySql(sql, entityClass);
    }

    public List<T> getBeans() {
        return this.getBeansBySql(SQL.WHERE());
    }

    public List<Map<String, Object>> getMapsBySql(SQL sqlWhere) {
        return this.getBaseDao().getMapsBySql(sqlWhere);
    }

    public List<Map<String, Object>> getMaps() {
        return this.getMapsBySql(SQL.WHERE());
    }

    // 通过id条件查询----------------------------
    //-----查询单个记录----通过id-----
    public Optional<T> getBeanOptById(Serializable id) {
        return Optional.ofNullable(getBeanById(id));
    }
    public T getById(Serializable id) {
        return this.getBeanById(id);
    }
    public T getBeanById(Serializable id) {
        return (T) getBaseDao().getBeanById(id, entityClass);
    }

    public Map<String, Object> getMapById(Serializable id) {
        return getBaseDao().getMapById(id);
    }

    //-----查询多个记录----通过id-----
    public List<T> getBeansByIds(Collection<? extends Serializable> idList) {
        return this.getBaseDao().getBeansByIdList(convertToArrayList(idList), entityClass);
    }

    public List<Map<String, Object>> getMapsByIds(Collection<? extends Serializable> idList) {
        return this.getBaseDao().getMapsByIds(convertToArrayList(idList));
    }


    public static ArrayList<Serializable> convertToArrayList(Iterable<? extends Serializable> ids) {
        // 使用 Stream API 将 Iterable 转换为 ArrayList
        return StreamSupport.stream(ids.spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // 通过Map条件查询---------------
    // 通过Map条件查询单个实体
    public T getBeanByMap(Map<String, Object> columnMap) {
        return (T) getBaseDao().getBeanByMap(columnMap, entityClass);
    }

    public Map<String, Object> getMapByMap(Map<String, Object> columnMap) {
        return getBaseDao().getMapByMap(columnMap);
    }

    // 通过Map条件查询列表
    public List<T> getBeansByMap(Map<String, Object> columnMap) {
        return getBaseDao().getBeansByMap(columnMap, entityClass);
    }

    public List<Map<String, Object>> getMapsByMap(Map<String, Object> columnMap) {
        return this.getBaseDao().getMapsByMap(columnMap);
    }

    //存在----------------------------------------------------
// 通过条件查询是否存在
    public boolean existsByMap(Map<String, Object> columnMap) {
        return getBaseDao().existsByMap(columnMap);
    }

    public boolean existsBySql(SQL sqlWhere) {
        return getBaseDao().existsBySql(sqlWhere);
    }
    public Long count(Map<String, Object> columnMap) {
        return this.countByMap(columnMap);
    }
    //---计数---------
    public Long countByMap(Map<String, Object> columnMap) {
        return getBaseDao().getCountByMap(columnMap);
    }

    public Long count(SQL sqlWhere) {
        return this.countBySql(sqlWhere);
    }
    public Long countBySql(SQL sqlWhere) {
        return getBaseDao().getCountBySql(sqlWhere);
    }
    public Long count(String fieldName, Object value) {
        return this.countByField(fieldName, value);
    }
    // 通过字段查询数量
    public Long countByField(String fieldName, Object value) {
        return getBaseDao().getCountByField(fieldName, value);
    }


    // 分页查询-------------------------------------------------


    public List<T> pageBeansBySql(PageModel<T> pageModel, SQL sqlWhere) {
        return this.getBaseDao().pageBeansBySql(pageModel, sqlWhere, entityClass);
    }
    public List<Map<String, Object>> pageMapsBySql(PageModel<T> pageModel, SQL sqlWhere) {
        return this.getBaseDao().pageMapsBySql(pageModel, sqlWhere);
    }

    public List<T> pageBeansAll(PageModel<T> pageModel) {
        return this.getBaseDao().pageBeansBySql(pageModel, SQL.WHERE(), entityClass);
    }
    public List<Map<String, Object>> pageMapsAll(PageModel<T> pageModel) {
        return this.getBaseDao().pageMapsBySql(pageModel, SQL.WHERE());
    }

    // 分页查询，返回PageModel对象

    public PageModel pageModelMapsBySql(PageModel<T> pageModel, SQL sqlWhere) {
        this.getBaseDao().pageMapsBySql(pageModel, sqlWhere);
        return pageModel;
    }

    public PageModel pageModelBeansBySql(PageModel<T> pageModel, SQL sqlWhere) {
        this.getBaseDao().pageBeansBySql(pageModel, sqlWhere,entityClass);
        return pageModel;
    }


}

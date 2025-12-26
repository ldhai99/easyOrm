package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.base.TaskType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ExecutorHandler<T extends ExecutorHandler<T>> extends BaseSQL<T> {


    //代理-------------------------------------------------执行类
    public void toExecutor() {
        self().toString();
    }


    //更新数据库----------------------------------------------------------------------------------------------------


    //更新
    public int update() {
        return getExecutor().update(self());
    }

    //增加，返回主键
    public Number insert() {
        return getExecutor().insert(self());
    }
    public Long getPrimaryKey() {
        return insert().longValue() ;
    }

    //执行存储过程
    public int execute() {

        return getExecutor().execute(self());
    }
    public Long executeGetKey() {
        return insert().longValue() ;
    }

    //查询数据库-----------------------------------------------------------------------------------
    public void ensureSelectTaskType() {
        ensureTaskType(self(), TaskType.SELECT);
    }
    //返回单列单行数据
    public String getString() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getString(self());
    }


    public Integer getInteger() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getInteger(self());
    }

    public Long getLong() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getLong(self());
    }


    public Float getFloat() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getFloat(self());
    }

    public Double getDouble() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getDouble(self());
    }

    public Number getNumber() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getNumber(self());
    }


    public BigDecimal getBigDecimal() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getBigDecimal(self());
    }

    public Date getDate() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getDate(self());
    }


    public <T> T getValue(Class<T> requiredType) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getValue(self(), requiredType);
    }

    //返回单列list数据
    public List<String> getStrings() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getStrings(self());
    }


    public List<Integer> getIntegers() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getIntegers(self());
    }


    public List<Long> getLongs() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getLongs(self());
    }


    public List<Double> getDoubles() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getDoubles(self());
    }


    public List<Float> getFloats() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getFloats(self());
    }


    public List<Number> getNumbers() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getNumbers(self());
    }


    public List<BigDecimal> getBigDecimals() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getBigDecimals(self());
    }


    public List<Date> getDates() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getDates(self());
    }


    public <T> List<T> getValues(Class<T> requiredType) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getValues(self(), requiredType);
    }


    //返回单行数据

    public Map<String, Object> getMap() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getMap(self());
    }
    public Map<String, Object> one() {
      return getMap();
    }

    //返回多行数据
    public List<Map<String, Object>> getMaps() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getMaps(self());
    }
    public List<Map<String, Object>> list() {
        return getMaps();
    }

    //返回Bean实体
    public <T> T one(Class<T> T) {
        return getBean(T);

    }
    public <T> T getBean(Class<T> T) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getBean(self(), T);
    }
    public <T> List<T> list(Class<T> T) {
        return getBeans(T);
    }


    //返回Bean list
    public <T> List<T> getBeans(Class<T> T) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return getExecutor().getBeans(self(), T);
    }



}

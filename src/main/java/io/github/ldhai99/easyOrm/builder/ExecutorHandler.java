package io.github.ldhai99.easyOrm.builder;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.base.TaskType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExecutorHandler<T extends ExecutorHandler<T>> extends GroupHandler<T> {


    //代理-------------------------------------------------执行类
    public void toExecutor() {
        self().toString();
    }


    //更新数据库----------------------------------------------------------------------------------------------------


    //更新
    public int update() {
        return executor.update(self());
    }

    //增加，返回主键
    public Number insert() {
        return executor.insert(self());
    }

    //执行存储过程
    public int execute() {

        return executor.execute(self());
    }
    //查询数据库-----------------------------------------------------------------------------------
    public void ensureSelectTaskType() {
        ensureTaskType(self(), TaskType.SELECT);
    }
    //返回单列单行数据
    public String getString() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getString(self());
    }


    public Integer getInteger() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getInteger(self());
    }

    public Long getLong() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getLong(self());
    }


    public Float getFloat() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getFloat(self());
    }

    public Double getDouble() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getDouble(self());
    }

    public Number getNumber() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getNumber(self());
    }


    public BigDecimal getBigDecimal() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getBigDecimal(self());
    }

    public Date getDate() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getDate(self());
    }


    public <T> T getValue(Class<T> requiredType) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getValue(self(), requiredType);
    }

    //返回单列list数据
    public List<String> getStrings() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getStrings(self());
    }


    public List<Integer> getIntegers() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getIntegers(self());
    }


    public List<Long> getLongs() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getLongs(self());
    }


    public List<Double> getDoubles() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getDoubles(self());
    }


    public List<Float> getFloats() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getFloats(self());
    }


    public List<Number> getNumbers() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getNumbers(self());
    }


    public List<BigDecimal> getBigDecimals() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getBigDecimals(self());
    }


    public List<Date> getDates() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getDates(self());
    }


    public <T> List<T> getValues(Class<T> requiredType) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getValues(self(), requiredType);
    }


    //返回单行数据

    public Map<String, Object> getMap() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getMap(self());
    }


    //返回多行数据
    public List<Map<String, Object>> getMaps() {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getMaps(self());
    }


    //返回Bean实体
    public <T> T getBean(Class<T> T) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getBean(self(), T);
    }


    //返回Bean list
    public <T> List<T> getBeans(Class<T> T) {
        ensureSelectTaskType(); // 校验任务类型为 SELECT
        return executor.getBeans(self(), T);
    }



}

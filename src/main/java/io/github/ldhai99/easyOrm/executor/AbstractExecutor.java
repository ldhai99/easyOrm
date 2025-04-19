package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.builder.ExecutorHandler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public abstract class AbstractExecutor implements Executor {
    //返回单列单行数据---------------------
    public String getString (ExecutorHandler sql) {
        return   getValue(sql,String.class);
    }

    public Integer getInteger (ExecutorHandler sql) {
        return   getValue(sql,Number.class).intValue();

    }
    public Long getLong (ExecutorHandler sql) {
        return   getValue(sql,Number.class).longValue();
    }


    public BigDecimal getBigDecimal (ExecutorHandler sql) {
        return   getValue(sql,BigDecimal.class);
    }
    public Float getFloat (ExecutorHandler sql) {
        return   getValue(sql,Number.class).floatValue();

    }
    public Double getDouble (ExecutorHandler sql) {
        return   getValue(sql,Number.class).doubleValue();

    }
    public Number getNumber (ExecutorHandler sql) {
        return   getValue(sql,Number.class);
    }

    public Date getDate (ExecutorHandler sql) {
        return   getValue(sql,Date.class);
    }


    //返回单列list数据
    public List<String> getStrings (ExecutorHandler sql) {
        return   this.getValues(sql, String.class);
    }

    public  List<Integer> getIntegers (ExecutorHandler sql) {
        return getNumbers(sql).stream().map(s -> s.intValue()).collect(Collectors.toList());

    }
    public  List<Long> getLongs (ExecutorHandler sql) {
        return getNumbers(sql).stream().map(s -> s.longValue()).collect(Collectors.toList());
    }

    public  List<BigDecimal> getDecimals (ExecutorHandler sql) {
        return   this.getValues(sql, BigDecimal.class);
    }
    public  List<Float> getFloats (ExecutorHandler sql) {
        return getNumbers(sql).stream().map(s -> s.floatValue()).collect(Collectors.toList());
    }
    public  List<Double> getDoubles (ExecutorHandler sql) {
        return getNumbers(sql).stream().map(s -> s.doubleValue()).collect(Collectors.toList());
    }
    public  List<Number> getNumbers (ExecutorHandler sql) {
        return   this.getValues(sql, Number.class);
    }
    public  List<BigDecimal> getBigDecimals (ExecutorHandler sql) {
        return   this.getValues(sql, BigDecimal.class);
    }
    public  List<Date> getDates (ExecutorHandler sql) {
        return   this.getValues(sql, Date.class);
    }


}

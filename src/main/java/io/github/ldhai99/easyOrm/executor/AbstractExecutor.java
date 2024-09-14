package io.github.ldhai99.easyOrm.executor;

import io.github.ldhai99.easyOrm.SQL;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public abstract class AbstractExecutor implements Executor {
    //返回单列单行数据---------------------
    public String getString (SQL sql) {
        return   getValue(sql,String.class);
    }

    public Integer getInteger (SQL sql) {
        return   getValue(sql,Number.class).intValue();

    }
    public Long getLong (SQL sql) {
        return   getValue(sql,Number.class).longValue();
    }


    public BigDecimal getBigDecimal (SQL sql) {
        return   getValue(sql,BigDecimal.class);
    }
    public Float getFloat (SQL sql) {
        return   getValue(sql,Number.class).floatValue();

    }
    public Double getDouble (SQL sql) {
        return   getValue(sql,Number.class).doubleValue();

    }
    public Number getNumber (SQL sql) {
        return   getValue(sql,Number.class);
    }

    public Date getDate (SQL sql) {
        return   getValue(sql,Date.class);
    }


    //返回单列list数据
    public List<String> getStrings (SQL sql) {
        return   this.getValues(sql, String.class);
    }

    public  List<Integer> getIntegers (SQL sql) {
        return getNumbers(sql).stream().map(s -> s.intValue()).collect(Collectors.toList());

    }
    public  List<Long> getLongs (SQL sql) {
        return getNumbers(sql).stream().map(s -> s.longValue()).collect(Collectors.toList());
    }

    public  List<BigDecimal> getDecimals (SQL sql) {
        return   this.getValues(sql, BigDecimal.class);
    }
    public  List<Float> getFloats (SQL sql) {
        return getNumbers(sql).stream().map(s -> s.floatValue()).collect(Collectors.toList());
    }
    public  List<Double> getDoubles (SQL sql) {
        return getNumbers(sql).stream().map(s -> s.doubleValue()).collect(Collectors.toList());
    }
    public  List<Number> getNumbers (SQL sql) {
        return   this.getValues(sql, Number.class);
    }
    public  List<BigDecimal> getBigDecimals (SQL sql) {
        return   this.getValues(sql, BigDecimal.class);
    }
    public  List<Date> getDates (SQL sql) {
        return   this.getValues(sql, Date.class);
    }
    //查询应用-------------判断是否存在------------------------------------------------

    public boolean isExists(SQL sql)  {
        if (this.getMaps(SQL.SELECT("dual").column("1").exists(sql)).size() >= 1)
            return true;
        else
            return false;
    }
    public Number getCount(SQL sql)  {
        if (sql.getBuilder().hasGroup()) {
            return this.getNumber(SQL.SELECT(sql.clone().setColumn("count(*) count"),"a")
                    .setColumn("count(*) count"));
        }else{
            return this.getNumber(sql.clone().setColumn("count(*) count"));
        }
    }
}

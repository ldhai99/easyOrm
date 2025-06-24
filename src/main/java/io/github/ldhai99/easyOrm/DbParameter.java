package io.github.ldhai99.easyOrm;



import io.github.ldhai99.easyOrm.base.DataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class DbParameter {
    // 使用枚举替代字符串类型，提高类型安全性


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final String name;
    private Object value;
    private DataType dataType;
    private boolean allowNull;

    // 主构造函数，其他构造函数通过this()调用它
    public DbParameter(String name, Object value) {
        this(name, value, DataType.STRING, true);
    }

    public DbParameter(String name, Object value, DataType dataType) {
        this(name, value, dataType, true);
    }

    public DbParameter(String name, Object value, DataType dataType, boolean allowNull) {
        this.name = Objects.requireNonNull(name, "Parameter name cannot be null");
        this.dataType = dataType != null ? dataType : DataType.STRING;
        this.allowNull = allowNull;
        setValue(value); // 使用setter确保值被正确处理
    }
    public void setValue(Object value) {
        if (value == null) {
            handleNullValue();
        }else
            this.value = value;
    }

    private void handleNullValue() {
        if (allowNull) {
            this.value = null;
        } else {
            this.value = getDefaultValue();
        }
    }
    private Object getDefaultValue() {
        switch (dataType) {
            case NUMBER: return 0;
            case DATE: return new Date();
            default: return "";
        }
    }
    private void convertValue(Object value) {
        try {
            switch (dataType) {
                case NUMBER:
                    this.value = parseDouble(value);
                    break;
                case DATE:
                    this.value = parseDate(value);
                    break;
                default:
                    this.value = value.toString().trim();
            }
        } catch (Exception e) {
            // 转换失败时使用默认值
            this.value = getDefaultValue();
        }
    }

    private double parseDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString().trim());
    }

    private Date parseDate(Object value) throws ParseException {
        if (value instanceof Date) {
            return (Date) value;
        }
        return DATE_FORMAT.parse(value.toString().trim());
    }
    public Object getValue() {
        return value;
    }
    public boolean isAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
        // 允许空值改变后重新处理值
        if (this.value == null) {
            handleNullValue();
        }
    }
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType != null ? dataType : DataType.STRING;
        // 数据类型改变后重新转换值
        if (this.value != null) {
            convertValue(this.value);
        }
    }
    public String getName() {
        return name;
    }
}

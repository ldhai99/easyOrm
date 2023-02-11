package io.github.ldhai99.easyOrm;



import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DbParameter {
    private String name;

    private Object value;



    private String datatype = "string";

    private boolean allowNull = false;

    public DbParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public DbParameter(String name, Object value, String datatype) {
        this.name = name;
        this.value = value;
        this.datatype = datatype;
    }

    public DbParameter(String name, Object value, String datatype,
                       boolean AllowNull) {
        this.name = name;
        this.value = value;
        this.datatype = datatype;
        this.allowNull = AllowNull;


        //数字型处理
        if (datatype.equalsIgnoreCase("double")) {

            if (value == null || value.toString().trim().length() == 0) {
                if (!allowNull)
                    this.value = 0;
                else
                    this.value = null;
            } else {
                try {
                    this.value = Double.parseDouble(value.toString());
                } catch (Exception e) {
                    this.value = 0;
                }
            }
        }

        //日期型处理
        else if (datatype.equalsIgnoreCase("date")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (value == null || value.toString().trim().length() == 0) {
                if (!allowNull)
                    this.value = new java.util.Date();
                else
                    this.value = null;
            } else {
                this.value = value.toString().trim();
                try {
                    this.value = sdf.parse(this.value.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (value == null ){
            if (!allowNull)
                this.value = " ";
            else
                this.value = null;
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public boolean isAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }
}

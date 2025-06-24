package io.github.ldhai99.easyOrm.base;

public enum DataType {
    STRING,
    NUMBER,
    DATE;



    /**
     * 获取数据类型的显示名称
     */
    public String getDisplayName() {
        switch (this) {
            case STRING: return "String";
            case NUMBER: return "Number";
            case DATE: return "Date";
            default: return "Unknown";
        }
    }
}

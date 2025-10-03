package io.github.ldhai99.easyOrm.test.enumtest;

/**
 * 姓名枚举类
 */
public enum NameEnum {

    /**
     * 张三
     */
    ZHANG_SAN("张三"),

    /**
     * 李四
     */
    LI_SI("李四");

    private final String name;

    NameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public final Object getValue() {
        return name;
    }
    /**
     * 根据姓名获取对应的枚举实例
     *
     * @param name 姓名
     * @return 对应的枚举，如果找不到则返回 null
     */
    public static NameEnum fromName(String name) {
        for (NameEnum e : values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }
}

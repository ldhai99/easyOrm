package io.github.ldhai99.easyOrm.test.enumtest;


import io.github.ldhai99.easyOrm.dbenum.DbEnum;

/**
 * 删除标志枚举类。
 */
public class AgeFlag extends DbEnum {

   public static final AgeFlag AGE18=new AgeFlag(18, "成年"); // 成年

    AgeFlag(Object value, String description) {
        super( value, description);
    }

}
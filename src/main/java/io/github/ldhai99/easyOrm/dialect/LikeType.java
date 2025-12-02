package io.github.ldhai99.easyOrm.dialect;

/**
 * LIKE类型枚举
 */
public enum LikeType {
    CONTAINS,       // 包含：%value%
    STARTS_WITH,    // 开始：value%
    ENDS_WITH,      // 结束：%value
    CUSTOM          // 自定义模式
}
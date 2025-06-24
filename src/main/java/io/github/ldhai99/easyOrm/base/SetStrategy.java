package io.github.ldhai99.easyOrm.base;

public enum SetStrategy {
    ALWAYS,         // 总是设置
    IF_NOT_NULL,    // 不为 null 时设置
    IF_NOT_EMPTY,   // 不为空时设置（广义空值）
    NEVER           // 从不设置（特殊用途）
}

package io.github.ldhai99.easyOrm.annotation;

public enum FieldStrategy {
    /** @deprecated */
    @Deprecated
    IGNORED,
    ALWAYS,
    NOT_NULL,
    NOT_EMPTY,
    DEFAULT,
    NEVER;

    private FieldStrategy() {
    }
}

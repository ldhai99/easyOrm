package io.github.ldhai99.easyOrm.dao.except;

public class TypeConvertException extends RuntimeException {
    public TypeConvertException(String message) {
        super(message);
    }

    public TypeConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
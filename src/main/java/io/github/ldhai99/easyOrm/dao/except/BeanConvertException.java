package io.github.ldhai99.easyOrm.dao.except;

// 自定义异常类
public class BeanConvertException extends RuntimeException {
    public BeanConvertException(String message) { super(message); }
    public BeanConvertException(String message, Throwable cause) { super(message, cause); }
}
package io.github.ldhai99.easyOrm.Lambda;

import java.io.Serializable;
import java.util.function.Function;


// 定义可序列化的函数式接口
@FunctionalInterface
public interface PropertyGetter<T> extends Function<T, Object>, Serializable {
}

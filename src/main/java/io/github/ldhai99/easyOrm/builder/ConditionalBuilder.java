package io.github.ldhai99.easyOrm.builder;

import java.util.function.Consumer;
import java.util.function.Function;

public class ConditionalBuilder<T> {
    private final T builder;
    private boolean condition;

    public ConditionalBuilder(T builder, boolean condition) {
        this.builder = builder;
        this.condition = condition;
    }

    public T then(Consumer<T> action) {
        if (condition) {
            action.accept(builder);
        }
        return builder;
    }
    /**
     * 上下文感知的 AND 组合条件
     *
     * @param condition 条件函数
     * @param context 上下文对象
     * @return 新的条件构建器
     */
    public <C> ConditionalBuilder<T> and(Function<C, Boolean> condition, C context) {
        return new ConditionalBuilder<>(builder, this.condition && condition.apply(context));
    }

    /**
     * 上下文感知的 OR 组合条件
     *
     * @param condition 条件函数
     * @param context 上下文对象
     * @return 新的条件构建器
     */
    public <C> ConditionalBuilder<T> or(Function<C, Boolean> condition, C context) {
        return new ConditionalBuilder<>(builder, this.condition || condition.apply(context));
    }
}


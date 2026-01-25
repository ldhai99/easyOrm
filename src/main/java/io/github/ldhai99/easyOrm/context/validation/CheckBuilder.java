package io.github.ldhai99.easyOrm.context.validation;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * 链式检查器
 *
 * @param <T> 对象类型
 */
public  class CheckBuilder<T> {
    private final T obj;
    private final String name;
    private boolean result = true;

    public CheckBuilder(T obj, String name) {
        this.obj = obj;
        this.name = name;
    }

    // 基本检查
    public CheckBuilder<T> isNull() {
        result = result && Checker.isNull(obj);
        return this;
    }

    public CheckBuilder<T> notNull() {
        result = result && Checker.notNull(obj);
        return this;
    }

    public CheckBuilder<T> isEmpty() {
        result = result && Checker.isEmpty(obj);
        return this;
    }

    public CheckBuilder<T> notEmpty() {
        result = result && Checker.notEmpty(obj);
        return this;
    }

    public CheckBuilder<T> isBlank() {
        if (obj instanceof CharSequence) {
            result = result && Checker.isBlank((CharSequence) obj);
        }
        return this;
    }

    public CheckBuilder<T> notBlank() {
        if (obj instanceof CharSequence) {
            result = result && Checker.notBlank((CharSequence) obj);
        }
        return this;
    }

    // 比较操作
    public CheckBuilder<T> eq(T other) {
        result = result && Checker.eq(obj, other);
        return this;
    }

    public CheckBuilder<T> notEq(T other) {
        result = result && Checker.notEq(obj, other);
        return this;
    }

    public CheckBuilder<T> gt(T other) {
        result = result && Checker.gt(obj, other);
        return this;
    }

    public CheckBuilder<T> gte(T other) {
        result = result && Checker.gte(obj, other);
        return this;
    }

    public CheckBuilder<T> lt(T other) {
        result = result && Checker.lt(obj, other);
        return this;
    }

    public CheckBuilder<T> lte(T other) {
        result = result && Checker.lte(obj, other);
        return this;
    }

    // 集合操作
    public CheckBuilder<T> in(Collection<T> collection) {
        result = result && Checker.in(obj, collection);
        return this;
    }

    @SafeVarargs
    public final CheckBuilder<T> in(T... values) {
        result = result && Checker.in(obj, values);
        return this;
    }

    public CheckBuilder<T> notIn(Collection<T> collection) {
        result = result && Checker.notIn(obj, collection);
        return this;
    }

    @SafeVarargs
    public final CheckBuilder<T> notIn(T... values) {
        result = result && Checker.notIn(obj, values);
        return this;
    }

    // 字符串操作
    public CheckBuilder<T> contains(String subStr) {
        if (obj instanceof String) {
            result = result && Checker.contains((String) obj, subStr);
        }
        return this;
    }

    public CheckBuilder<T> notContains(String subStr) {
        if (obj instanceof String) {
            result = result && Checker.notContains((String) obj, subStr);
        }
        return this;
    }

    public CheckBuilder<T> startsWith(String prefix) {
        if (obj instanceof String) {
            result = result && Checker.startsWith((String) obj, prefix);
        }
        return this;
    }

    public CheckBuilder<T> endsWith(String suffix) {
        if (obj instanceof String) {
            result = result && Checker.endsWith((String) obj, suffix);
        }
        return this;
    }

    public CheckBuilder<T> notStartsWith(String prefix) {
        if (obj instanceof String) {
            result = result && Checker.notStartsWith((String) obj, prefix);
        }
        return this;
    }

    public CheckBuilder<T> notEndsWith(String suffix) {
        if (obj instanceof String) {
            result = result && Checker.notEndsWith((String) obj, suffix);
        }
        return this;
    }

    public CheckBuilder<T> like(String pattern) {
        if (obj instanceof String) {
            result = result && Checker.like((String) obj, pattern);
        }
        return this;
    }

    public CheckBuilder<T> notLike(String pattern) {
        if (obj instanceof String) {
            result = result && Checker.notLike((String) obj, pattern);
        }
        return this;
    }

    public CheckBuilder<T> likeWithAnd(String... keywords) {
        if (obj instanceof String) {
            result = result && Checker.likeWithAnd((String) obj, keywords);
        }
        return this;
    }

    public CheckBuilder<T> likeWithOr(String... keywords) {
        if (obj instanceof String) {
            result = result && Checker.likeWithOr((String) obj, keywords);
        }
        return this;
    }

    // 范围操作
    public CheckBuilder<T> between(T min, T max) {
        result = result && Checker.between(obj, min, max);
        return this;
    }

    public CheckBuilder<T> notBetween(T min, T max) {
        result = result && Checker.notBetween(obj, min, max);
        return this;
    }

    // 自定义检查
    public CheckBuilder<T> check(Function<T, Boolean> predicate) {
        if (obj != null) {
            result = result && predicate.apply(obj);
        }
        return this;
    }

    /**
     * 获取检查结果
     */
    public boolean getResult() {
        return result;
    }

    /**
     * 断言检查结果为true
     */
    public void verify() {
        if (!result) {
            throw new IllegalStateException("Check failed for: " + name);
        }
    }

    /**
     * 断言检查结果为true，自定义异常信息
     */
    public void verify(String message) {
        if (!result) {
            throw new IllegalStateException(message);
        }
    }
    // 在 CheckBuilder 类中补充：
    public CheckBuilder<T> isPositive() {
        if (obj instanceof Number) {
            result = result && Checker.isPositive((Number) obj);
        }
        return this;
    }

    public CheckBuilder<T> isNotNegative() {
        if (obj instanceof Number) {
            result = result && Checker.isNotNegative((Number) obj);
        }
        return this;
    }

    public CheckBuilder<T> isNegative() {
        if (obj instanceof Number) {
            result = result && Checker.isNegative((Number) obj);
        }
        return this;
    }

    public CheckBuilder<T> isZero() {
        if (obj instanceof Number) {
            result = result && Checker.isZero((Number) obj);
        }
        return this;
    }

    public CheckBuilder<T> isNotZero() {
        if (obj instanceof Number) {
            result = result && Checker.isNotZero((Number) obj);
        }
        return this;
    }

    public CheckBuilder<T> inRange(Number min, Number max) {
        if (obj instanceof Number) {
            result = result && Checker.inRange((Number) obj, min, max);
        }
        return this;
    }

    public CheckBuilder<T> notInRange(Number min, Number max) {
        if (obj instanceof Number) {
            result = result && Checker.notInRange((Number) obj, min, max);
        }
        return this;
    }
    // 数组检查
    @SuppressWarnings("unchecked")
    public CheckBuilder<T> containsInArray(T element) {
        if (obj != null && obj.getClass().isArray()) {
            result = result && Checker.in(element, obj);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public CheckBuilder<T> notContainsInArray(T element) {
        if (obj != null && obj.getClass().isArray()) {
            result = result && Checker.notIn(element, obj);
        }
        return this;
    }

    // Map检查
    public CheckBuilder<T> containsKey(Object key) {
        if (obj instanceof Map) {
            result = result && Checker.containsKey((Map<?, ?>) obj, key);
        }
        return this;
    }

    public CheckBuilder<T> notContainsKey(Object key) {
        if (obj instanceof Map) {
            result = result && Checker.notContainsKey((Map<?, ?>) obj, key);
        }
        return this;
    }

    public CheckBuilder<T> containsValue(Object value) {
        if (obj instanceof Map) {
            result = result && Checker.containsValue((Map<?, ?>) obj, value);
        }
        return this;
    }

    public CheckBuilder<T> notContainsValue(Object value) {
        if (obj instanceof Map) {
            result = result && Checker.notContainsValue((Map<?, ?>) obj, value);
        }
        return this;
    }
    public CheckBuilder<T> isInstance(Class<?> type) {
        result = result && Checker.isInstance(obj, type);
        return this;
    }

    public CheckBuilder<T> notInstance(Class<?> type) {
        result = result && Checker.notInstance(obj, type);
        return this;
    }
    // 在 CheckBuilder 类末尾补充：
    /**
     * 如果检查失败则返回默认值
     */
    public T orElse(T defaultValue) {
        return result ? obj : defaultValue;
    }

    /**
     * 如果检查失败则从Supplier获取默认值
     */
    public T orElseGet(java.util.function.Supplier<T> defaultValueSupplier) {
        return result ? obj : (defaultValueSupplier != null ? defaultValueSupplier.get() : null);
    }

    /**
     * 如果检查失败则返回Optional.empty()
     */
    public java.util.Optional<T> toOptional() {
        return result ? java.util.Optional.ofNullable(obj) : java.util.Optional.empty();
    }

    /**
     * 应用转换函数，但仅在检查通过时
     */
    public <R> R map(java.util.function.Function<T, R> mapper) {
        return result && obj != null ? mapper.apply(obj) : null;
    }

    /**
     * 应用转换函数，但仅在检查通过时，并提供默认值
     */
    public <R> R mapOrElse(java.util.function.Function<T, R> mapper, R defaultValue) {
        return result && obj != null ? mapper.apply(obj) : defaultValue;
    }

    /**
     * 如果检查通过，则执行消费者函数
     */
    public CheckBuilder<T> ifPresent(java.util.function.Consumer<T> consumer) {
        if (result && obj != null) {
            consumer.accept(obj);
        }
        return this;
    }

    /**
     * 如果检查失败，则执行动作
     */
    public CheckBuilder<T> ifAbsent(Runnable action) {
        if (!result) {
            action.run();
        }
        return this;
    }

    /**
     * 安全获取 - 检查通过才返回对象，否则返回null
     */
    public T getIfValid() {
        return result ? obj : null;
    }
    public CheckBuilder<T> and(boolean condition) {
        result = result && condition;
        return this;
    }

    public CheckBuilder<T> or(boolean condition) {
        result = result || condition;
        return this;
    }
    /**
     * 检查字符串长度
     */
    public CheckBuilder<T> lengthBetween(int min, int max) {
        if (obj instanceof CharSequence) {
            int length = ((CharSequence) obj).length();
            result = result && length >= min && length <= max;
        }
        return this;
    }

    /**
     * 检查集合大小
     */
    public CheckBuilder<T> sizeBetween(int min, int max) {
        if (obj instanceof Collection) {
            int size = ((Collection<?>) obj).size();
            result = result && size >= min && size <= max;
        } else if (obj instanceof Map) {
            int size = ((Map<?, ?>) obj).size();
            result = result && size >= min && size <= max;
        }
        return this;
    }
    /**
     * 获取检查的对象
     */
    public T get() {
        return obj;
    }
}
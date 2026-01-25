package io.github.ldhai99.easyOrm.context.validation;


/**
 * 数组专用的链式检查器
 */
public  class ArrayCheckBuilder<T> {
    private final T[] array;
    private final String name;
    private boolean result = true;

    public ArrayCheckBuilder(T[] array, String name) {
        this.array = array;
        this.name = name;
    }

    public ArrayCheckBuilder<T> contains(T element) {
        result = result && Checker.in(element, array);
        return this;
    }

    public ArrayCheckBuilder<T> notContains(T element) {
        result = result && Checker.notIn(element, array);
        return this;
    }

    public ArrayCheckBuilder<T> isEmpty() {
        result = result && Checker.isEmpty(array);
        return this;
    }

    public ArrayCheckBuilder<T> notEmpty() {
        result = result && Checker.notEmpty(array);
        return this;
    }

    /**
     * 检查数组长度
     */
    public ArrayCheckBuilder<T> hasLength(int expectedLength) {
        result = result && (array != null && array.length == expectedLength);
        return this;
    }

    /**
     * 检查数组长度范围
     */
    public ArrayCheckBuilder<T> lengthBetween(int min, int max) {
        if (array != null) {
            int length = array.length;
            result = result && (length >= min && length <= max);
        }
        return this;
    }

    /**
     * 检查所有元素都满足条件
     */
    public ArrayCheckBuilder<T> allMatch(java.util.function.Predicate<T> predicate) {
        if (array != null) {
            for (T element : array) {
                if (!predicate.test(element)) {
                    result = false;
                    break;
                }
            }
        }
        return this;
    }

    /**
     * 检查至少有一个元素满足条件
     */
    public ArrayCheckBuilder<T> anyMatch(java.util.function.Predicate<T> predicate) {
        if (array != null) {
            boolean found = false;
            for (T element : array) {
                if (predicate.test(element)) {
                    found = true;
                    break;
                }
            }
            result = result && found;
        } else {
            result = false;
        }
        return this;
    }

    public boolean getResult() {
        return result;
    }
}


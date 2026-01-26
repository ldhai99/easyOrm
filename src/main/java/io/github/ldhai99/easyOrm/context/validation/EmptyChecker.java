package io.github.ldhai99.easyOrm.context.validation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class EmptyChecker {

    // ================ Null 判断 ================

    /**
     * 检查对象是否为null
     *
     * @param obj 要检查的对象
     * @return 如果对象为null则返回true
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 检查对象是否不为null
     *
     * @param obj 要检查的对象
     * @return 如果对象不为null则返回true
     */
    public static boolean notNull(Object obj) {
        return obj != null;
    }

    // ================ Empty 判断 ================

    /**
     * 检查对象是否为空
     * <p>支持的类型：</p>
     * <ul>
     *   <li>null → true</li>
     *   <li>CharSequence（字符串）→ length == 0</li>
     *   <li>Collection → isEmpty()</li>
     *   <li>Map → isEmpty()</li>
     *   <li>数组 → length == 0</li>
     *   <li>Optional → !isPresent()</li>
     *   <li>Stream → 没有元素</li>
     *   <li>其他对象 → false（非null即非空）</li>
     * </ul>
     *
     * @param obj 要检查的对象
     * @return 如果对象为空则返回true
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        if (obj instanceof java.util.stream.BaseStream) {
            return !((java.util.stream.BaseStream<?, ?>) obj).iterator().hasNext();
        }

        return false; // 其他对象（POJO/Date/Number）非null即非空
    }

    /**
     * 检查对象是否不为空
     *
     * @param obj 要检查的对象
     * @return 如果对象不为空则返回true
     * @see #isEmpty(Object)
     */
    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    // ================ Blank 判断（仅字符串） ================

    /**
     * 检查字符序列是否为空或只包含空白字符
     *
     * @param cs 要检查的字符序列，可以为null
     * @return 如果为null、空字符串或只包含空白字符则返回true
     */
    public static boolean isBlank(CharSequence cs) {
        if (cs == null) {
            return true;
        }

        int length = cs.length();
        if (length == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (cs.charAt(i) > ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符序列是否不为空且包含非空白字符
     *
     * @param cs 要检查的字符序列，可以为null
     * @return 如果不为null、非空字符串且包含非空白字符则返回true
     */
    public static boolean notBlank(CharSequence cs) {
        return !isBlank(cs);
    }
}

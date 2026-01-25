package io.github.ldhai99.easyOrm.context.validation;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.function.Function;

/**
 * 综合性检查工具类
 *
 * 命名规范：
 * - 肯定形式：isXxx()    → isNull(), isEmpty(), isBlank(), equals(), contains()
 * - 否定形式：notXxx()   → notNull(), notEmpty(), notBlank(), notEquals(), notContains()
 * - 驼峰命名对应 QueryOperator.standardCode
 *
 * 对应 QueryOperator.standardCode 映射：
 *   gt           → gt()
 *   gte          → gte()
 *   lt           → lt()
 *   lte          → lte()
 *   eq           → eq()
 *   neq          → neq()
 *   in           → in()
 *   not_in       → notIn()
 *   like         → like()
 *   not_like     → notLike()
 *   like_left    → startsWith()
 *   like_right   → endsWith()
 *   not_like_left  → notStartsWith()
 *   not_like_right → notEndsWith()
 *   contains     → contains()
 *   not_contains → notContains()
 *   starts_with  → startsWith()
 *   ends_with    → endsWith()
 *   not_starts_with → notStartsWith()
 *   not_ends_with   → notEndsWith()
 *   is_null      → isNull()
 *   is_not_null  → notNull()
 *   empty        → isEmpty()
 *   not_empty    → notEmpty()
 *   range        → between()
 *   not_range    → notBetween()
 *
 * @author ldhai99
 * @since 1.0
 */
public final class Checker {

    private Checker() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ================ Null 判断 ================

    /**
     * 检查对象是否为null
     * 对应 QueryOperator.is_null
     *
     * @param obj 要检查的对象
     * @return 如果对象为null则返回true
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 检查对象是否不为null
     * 对应 QueryOperator.is_not_null
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
     * 对应 QueryOperator.empty
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
     * 对应 QueryOperator.not_empty
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

    // ================ 基本比较操作符 ================

    /**
     * 检查a是否大于b
     * 对应 QueryOperator.gt
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果a大于b则返回true
     */
    public static boolean gt(Object a, Object b) {
        if (a == null || b == null) {
            return false;  // null 参与比较直接返回 false
        }
        return compare(a, b) > 0;
    }

    /**
     * 检查a是否大于等于b
     * 对应 QueryOperator.gte
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果a大于等于b则返回true
     */
    public static boolean gte(Object a, Object b) {
        if (a == null || b == null) {
            return false;  // null 参与比较直接返回 false
        }
        return compare(a, b) >= 0;
    }

    /**
     * 检查a是否小于b
     * 对应 QueryOperator.lt
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果a小于b则返回true
     */

    public static boolean lt(Object a, Object b) {
        if (a == null || b == null) {
            return false;  // null 参与比较直接返回 false
        }
        return compare(a, b) < 0;
    }

    /**
     * 检查a是否小于等于b
     * 对应 QueryOperator.lte
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果a小于等于b则返回true
     */
    public static boolean lte(Object a, Object b) {
        if (a == null || b == null) {
            return false;  // null 参与比较直接返回 false
        }
        return compare(a, b) <= 0;
    }


    /**
     * 检查两个对象是否相等（null安全）
     * 对应 QueryOperator.eq
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果相等则返回true
     */
    public static boolean eq(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }

        // 处理基本类型数组
        if (a.getClass().isArray() && b.getClass().isArray()) {
            return arrayEquals(a, b);
        }

        return a.equals(b);
    }

    /**
     * 检查两个对象是否不相等（null安全）
     * 对应 QueryOperator.neq
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果不相等则返回true
     */
    public static boolean notEq(Object a, Object b) {
        return !eq(a, b);
    }

    /**
     * 深度比较两个数组是否相等
     */
    private static boolean arrayEquals(Object a, Object b) {
        int length = Array.getLength(a);
        if (length != Array.getLength(b)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            Object elemA = Array.get(a, i);
            Object elemB = Array.get(b, i);

            if (!Objects.equals(elemA, elemB)) {
                return false;
            }
        }
        return true;
    }

    // ================ 集合操作符 ================

    /**
     * 检查元素是否在集合中
     * 对应 QueryOperator.in
     *
     * @param element    要检查的元素
     * @param collection 集合
     * @return 如果元素在集合中则返回true
     */
    public static boolean in(Object element, Collection<?> collection) {
        if (collection == null || element == null) {
            return false;
        }
        return collection.contains(element);
    }

    /**
     * 检查元素是否在数组中
     * 对应 QueryOperator.in
     *
     * @param element 要检查的元素
     * @param array   数组
     * @return 如果元素在数组中则返回true
     */
    public static boolean in(Object element, Object array) {
        if (array == null || !array.getClass().isArray() || element == null) {
            return false;
        }

        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object elem = Array.get(array, i);
            if (Objects.equals(elem, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查元素是否在变长参数中
     * 对应 QueryOperator.in
     *
     * @param element 要检查的元素
     * @param values  值列表
     * @return 如果元素在值列表中则返回true
     */
    @SafeVarargs
    public static <T> boolean in(T element, T... values) {
        if (values == null || element == null) {
            return false;
        }
        for (T value : values) {
            if (Objects.equals(element, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查元素是否不在集合中
     * 对应 QueryOperator.not_in
     *
     * @param element    要检查的元素
     * @param collection 集合
     * @return 如果元素不在集合中则返回true
     */
    public static boolean notIn(Object element, Collection<?> collection) {
        return !in(element, collection);
    }

    /**
     * 检查元素是否不在数组中
     * 对应 QueryOperator.not_in
     *
     * @param element 要检查的元素
     * @param array   数组
     * @return 如果元素不在数组中则返回true
     */
    public static boolean notIn(Object element, Object array) {
        return !in(element, array);
    }

    /**
     * 检查元素是否不在变长参数中
     * 对应 QueryOperator.not_in
     *
     * @param element 要检查的元素
     * @param values  值列表
     * @return 如果元素不在值列表中则返回true
     */
    @SafeVarargs
    public static <T> boolean notIn(T element, T... values) {
        return !in(element, values);
    }

    // ================ 模糊查询操作符 ================

    /**
     * 检查字符串是否匹配模式（全模糊）
     * 对应 QueryOperator.like
     *
     * @param str     要检查的字符串
     * @param pattern 模式，支持 % 和 _ 通配符
     * @return 如果匹配则返回true
     */
    public static boolean like(String str, String pattern) {
        if (str == null || pattern == null) {
            return false;
        }
        // 将 SQL LIKE 模式转换为正则表达式
        String regex = pattern
                .replace("%", ".*")
                .replace("_", ".");
        return str.matches(regex);
    }

    /**
     * 检查字符串是否不匹配模式
     * 对应 QueryOperator.not_like
     *
     * @param str     要检查的字符串
     * @param pattern 模式，支持 % 和 _ 通配符
     * @return 如果不匹配则返回true
     */
    public static boolean notLike(String str, String pattern) {
        return !like(str, pattern);
    }

    /**
     * 检查字符串是否以指定前缀开头（左模糊）
     * 对应 QueryOperator.like_left 和 QueryOperator.starts_with
     *
     * @param str    要检查的字符串
     * @param prefix 前缀
     * @return 如果以指定前缀开头则返回true
     */
    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    /**
     * 检查字符串是否以指定后缀结尾（右模糊）
     * 对应 QueryOperator.like_right 和 QueryOperator.ends_with
     *
     * @param str    要检查的字符串
     * @param suffix 后缀
     * @return 如果以指定后缀结尾则返回true
     */
    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.endsWith(suffix);
    }

    /**
     * 检查字符串是否不以指定前缀开头
     * 对应 QueryOperator.not_like_left 和 QueryOperator.not_starts_with
     *
     * @param str    要检查的字符串
     * @param prefix 前缀
     * @return 如果不以指定前缀开头则返回true
     */
    public static boolean notStartsWith(String str, String prefix) {
        return !startsWith(str, prefix);
    }

    /**
     * 检查字符串是否不以指定后缀结尾
     * 对应 QueryOperator.not_like_right 和 QueryOperator.not_ends_with
     *
     * @param str    要检查的字符串
     * @param suffix 后缀
     * @return 如果不以指定后缀结尾则返回true
     */
    public static boolean notEndsWith(String str, String suffix) {
        return !endsWith(str, suffix);
    }

    /**
     * 检查字符串是否包含子字符串
     * 对应 QueryOperator.contains
     *
     * @param str     要检查的字符串
     * @param subStr  子字符串
     * @return 如果包含子字符串则返回true
     */
    public static boolean contains(String str, String subStr) {
        if (str == null || subStr == null) {
            return false;
        }
        return str.contains(subStr);
    }

    /**
     * 检查字符串是否不包含子字符串
     * 对应 QueryOperator.not_contains
     *
     * @param str     要检查的字符串
     * @param subStr  子字符串
     * @return 如果不包含子字符串则返回true
     */
    public static boolean notContains(String str, String subStr) {
        return !contains(str, subStr);
    }

    // ================ 多关键词匹配 ================

    /**
     * 多关键词 AND 匹配
     * 对应 QueryOperator.like_with_and
     *
     * @param str       要检查的字符串
     * @param keywords  关键词数组
     * @return 如果包含所有关键词则返回true
     *         如果str为null返回false
     *         如果keywords为null返回false
     *         如果没有关键词（keywords.length == 0）返回true（空条件总是满足）
     */
    public static boolean likeWithAnd(String str, String... keywords) {
        if (str == null) {
            return false;  // null 字符串不匹配任何内容
        }

        if (keywords == null) {
            return false;  // null 关键词数组视为无效输入
        }

        // 没有关键词 → 空 AND 条件总是为 true
        if (keywords.length == 0) {
            return true;
        }

        for (String keyword : keywords) {
            if (keyword != null && !str.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 多关键词 OR 匹配
     * 对应 QueryOperator.like_with_or
     *
     * @param str       要检查的字符串
     * @param keywords  关键词数组
     * @return 如果包含任意关键词则返回true
     *         如果str为null返回false
     *         如果keywords为null返回false
     *         如果没有关键词（keywords.length == 0）返回false（空条件不满足）
     */
    public static boolean likeWithOr(String str, String... keywords) {
        if (str == null) {
            return false;  // null 字符串不匹配任何内容
        }

        if (keywords == null) {
            return false;  // null 关键词数组视为无效输入
        }

        // 没有关键词 → 空 OR 条件总是为 false
        if (keywords.length == 0) {
            return false;
        }

        for (String keyword : keywords) {
            if (keyword != null && str.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    // ================ 范围查询 ================

    /**
     * 检查值是否在指定范围内（包含边界）
     * 对应 QueryOperator.range
     *
     * @param value 要检查的值
     * @param min   最小值
     * @param max   最大值
     * @return 如果在范围内则返回true
     */
    public static boolean between(Object value, Object min, Object max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        return gte(value, min) && lte(value, max);
    }

    /**
     * 检查值是否在指定范围内（不包含边界）
     *
     * @param value 要检查的值
     * @param min   最小值
     * @param max   最大值
     * @return 如果在范围内则返回true
     */
    public static boolean betweenExclusive(Object value, Object min, Object max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        return gt(value, min) && lt(value, max);
    }

    /**
     * 检查值是否不在指定范围内（包含边界）
     * 对应 QueryOperator.not_range
     *
     * @param value 要检查的值
     * @param min   最小值
     * @param max   最大值
     * @return 如果不在范围内则返回true
     */
    public static boolean notBetween(Object value, Object min, Object max) {
        return !between(value, min, max);
    }

    /**
     * 检查值是否不在指定范围内（不包含边界）
     *
     * @param value 要检查的值
     * @param min   最小值
     * @param max   最大值
     * @return 如果不在范围内则返回true
     */
    public static boolean notBetweenExclusive(Object value, Object min, Object max) {
        return !betweenExclusive(value, min, max);
    }

    // ================ 比较方法 ================

    /**
     * 比较两个Comparable对象的大小
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 比较结果：-1(a<b), 0(a==b), 1(a>b)
     * @throws NullPointerException 如果任意参数为null
     */
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public static <T extends Comparable<T>> int compare(T a, T b) {
//        return a.compareTo(b);
//    }

    /**
     * 比较两个对象的大小（支持自动拆箱和类型转换）
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 比较结果：-1(a<b), 0(a==b), 1(a>b)
     *         如果两者都为null返回0
     *         如果a为null，b不为null返回-1
     *         如果a不为null，b为null返回1
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int compare(Object a, Object b) {
        // 首先处理 null 值
        if (a == null && b == null) return 0;
        if (a == null) return -1;  // null 小于任何非null值
        if (b == null) return 1;   // 任何非null值大于null

        // 如果都是Comparable类型
        if (a instanceof Comparable && b instanceof Comparable) {
            try {
                // 尝试直接比较
                if (a.getClass().isAssignableFrom(b.getClass()) ||
                        b.getClass().isAssignableFrom(a.getClass())) {
                    return ((Comparable) a).compareTo(b);
                }
            } catch (ClassCastException e) {
                // 类型不匹配，尝试数值比较
            }
        }

        // 尝试数值比较
        Number numA = toNumber(a);
        Number numB = toNumber(b);
        if (numA != null && numB != null) {
            return compareNumbers(numA, numB);
        }

        // 尝试日期时间比较
        if (a instanceof Temporal && b instanceof Temporal) {
            // 简单的toString比较（实际项目中可能需要更精确的比较）
            return a.toString().compareTo(b.toString());
        }

        // 默认使用字符串比较
        return a.toString().compareTo(b.toString());
    }

    /**
     * 将对象转换为Number（支持所有数值类型）
     */
    private static Number toNumber(Object obj) {
        if (obj instanceof Number) {
            return (Number) obj;
        }

        if (obj instanceof Character) {
            return (int) (Character) obj;
        }

        if (obj instanceof Boolean) {
            return ((Boolean) obj) ? 1 : 0;
        }

        // 尝试从字符串解析
        if (obj instanceof CharSequence) {
            try {
                String str = obj.toString().trim();
                if (str.contains(".") || str.contains("e") || str.contains("E")) {
                    return Double.parseDouble(str);
                } else {
                    return new BigInteger(str);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * 比较两个Number对象
     */
    private static int compareNumbers(Number a, Number b) {
        // 使用BigDecimal进行精确比较
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        return bdA.compareTo(bdB);
    }

    /**
     * 将Number转换为BigDecimal
     */
    private static BigDecimal toBigDecimal(Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }
        if (number instanceof Long ||
                number instanceof Integer ||
                number instanceof Short ||
                number instanceof Byte) {
            return BigDecimal.valueOf(number.longValue());
        }
        if (number instanceof Double || number instanceof Float) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        // 其他类型转换为字符串再解析
        return new BigDecimal(number.toString());
    }

    // ================ 类型检查 ================

    /**
     * 检查对象是否为指定类型
     *
     * @param obj  要检查的对象
     * @param type 目标类型
     * @return 如果对象是指定类型则返回true
     */
    public static boolean isInstance(Object obj, Class<?> type) {
        return type != null && type.isInstance(obj);
    }

    /**
     * 检查对象是否不为指定类型
     *
     * @param obj  要检查的对象
     * @param type 目标类型
     * @return 如果对象不是指定类型则返回true
     */
    public static boolean notInstance(Object obj, Class<?> type) {
        return !isInstance(obj, type);
    }

    // ================ 工具方法 ================

    /**
     * 如果对象为空则返回默认值
     *
     * @param obj          要检查的对象
     * @param defaultValue 默认值
     * @param <T>          对象类型
     * @return 如果对象不为空则返回对象本身，否则返回默认值
     */
    public static <T> T defaultIfEmpty(T obj, T defaultValue) {
        return isEmpty(obj) ? defaultValue : obj;
    }

    /**
     * 如果字符串为空或全是空白字符则返回默认值
     *
     * @param str          要检查的字符串
     * @param defaultValue 默认值
     * @return 如果字符串不为空且不全是空白字符则返回字符串本身，否则返回默认值
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * 安全地获取字符串长度（null安全）
     *
     * @param cs 字符序列
     * @return 字符串长度，如果为null则返回0
     */
    public static int safeLength(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * 安全地获取集合大小（null安全）
     *
     * @param collection 集合
     * @return 集合大小，如果为null则返回0
     */
    public static int safeSize(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * 安全地获取Map大小（null安全）
     *
     * @param map Map
     * @return Map大小，如果为null则返回0
     */
    public static int safeSize(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * 安全地获取数组长度（null安全）
     *
     * @param array 数组
     * @return 数组长度，如果为null则返回0
     */
    public static int safeLength(Object array) {
        if (array == null) {
            return 0;
        }
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Object is not an array");
        }
        return Array.getLength(array);
    }

    /**
     * 获取对象的字符串表示（null安全）
     *
     * @param obj 对象
     * @return 对象的字符串表示，如果为null则返回空字符串
     */
    public static String safeToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 获取对象的字符串表示，使用自定义null值
     *
     * @param obj     对象
     * @param nullStr null时的替代字符串
     * @return 对象的字符串表示
     */
    public static String safeToString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    // ================ 链式API ================
    /**
     * 专门为数组创建链式检查器
     */
    public static <T> ArrayCheckBuilder<T> thatArray(T[] array, String name) {
        return new ArrayCheckBuilder<>(array, name);
    }

    /**
     * 数组专用的链式检查器
     */
    public static class ArrayCheckBuilder<T> {
        private final T[] array;
        private final String name;
        private boolean result = true;

        private ArrayCheckBuilder(T[] array, String name) {
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
    /**
     * 创建链式检查器
     *
     * @param obj  要检查的对象
     * @param name 对象名称
     * @param <T>  对象类型
     * @return 检查器实例
     */
    public static <T> CheckBuilder<T> that(T obj, String name) {
        return new CheckBuilder<>(obj, name);
    }

    /**
     * 链式检查器
     *
     * @param <T> 对象类型
     */
    public static class CheckBuilder<T> {
        private final T obj;
        private final String name;
        private boolean result = true;

        private CheckBuilder(T obj, String name) {
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

// ================ 带缺省值的方法 ================

    /**
     * 如果对象为null则返回默认值，否则返回对象本身
     *
     * @param obj          要检查的对象
     * @param defaultValue 默认值
     * @param <T>          对象类型
     * @return 如果对象不为null则返回对象本身，否则返回默认值
     */
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * 如果字符串为空或空白则返回默认值，否则返回trim后的字符串
     *
     * @param str          要检查的字符串
     * @param defaultValue 默认值
     * @return 如果字符串不为空且不全是空白字符则返回trim后的字符串，否则返回默认值
     */
    public static String defaultIfBlankTrim(String str, String defaultValue) {
        return notBlank(str) ? str.trim() : defaultValue;
    }

    /**
     * 获取第一个非null值
     *
     * @param values 值列表
     * @param <T>    值类型
     * @return 第一个非null值，如果全部为null则返回null
     */
    @SafeVarargs
    public static <T> T firstNotNull(T... values) {
        if (values == null) {
            return null;
        }
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空值
     * 注意：对于字符串，只检查长度不为0，不检查是否空白
     *
     * @param values 值列表
     * @param <T>    值类型
     * @return 第一个非空值，如果全部为空则返回null
     */
    @SafeVarargs
    public static <T> T firstNotEmpty(T... values) {
        if (values == null) {
            return null;
        }
        for (T value : values) {
            if (notEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空白字符串值
     *
     * @param values 字符串列表
     * @return 第一个非空白字符串值，如果全部为空白则返回null
     */
    @SafeVarargs
    public static String firstNotBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (notBlank(value)) {
                return value;
            }
        }
        return null;
    }










// ================ 逻辑操作 ================

    /**
     * 所有条件都满足
     *
     * @param conditions 条件数组
     * @return 如果所有条件都满足则返回true
     */
    public static boolean all(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }
        return true;
    }

    /**
     * 任意条件满足
     *
     * @param conditions 条件数组
     * @return 如果任意条件满足则返回true
     */
    public static boolean any(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        return false;
    }

    /**
     * 没有条件满足
     *
     * @param conditions 条件数组
     * @return 如果没有条件满足则返回true
     */
    public static boolean none(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return true;
        }
        for (boolean condition : conditions) {
            if (condition) {
                return false;
            }
        }
        return true;
    }

// ================ 数值检查 ================

    /**
     * 检查数值是否为正数
     *
     * @param number 数值
     * @return 如果为正数则返回true
     */
    public static boolean isPositive(Number number) {
        if (number == null) {
            return false;
        }
        return compareNumbers(number, 0) > 0;
    }

    /**
     * 检查数值是否为非负数
     *
     * @param number 数值
     * @return 如果为非负数则返回true
     */
    public static boolean isNotNegative(Number number) {
        if (number == null) {
            return false;
        }
        return compareNumbers(number, 0) >= 0;
    }

    /**
     * 检查数值是否为负数
     *
     * @param number 数值
     * @return 如果为负数则返回true
     */
    public static boolean isNegative(Number number) {
        if (number == null) {
            return false;
        }
        return compareNumbers(number, 0) < 0;
    }

    /**
     * 检查数值是否为零
     *
     * @param number 数值
     * @return 如果为零则返回true
     */
    public static boolean isZero(Number number) {
        if (number == null) {
            return false;
        }
        return compareNumbers(number, 0) == 0;
    }

    /**
     * 检查数值是否不为零
     *
     * @param number 数值
     * @return 如果不为零则返回true
     */
    public static boolean isNotZero(Number number) {
        if (number == null) {
            return false;
        }
        return compareNumbers(number, 0) != 0;
    }

    /**
     * 检查数值是否在范围内
     *
     * @param value 要检查的数值
     * @param min   最小值
     * @param max   最大值
     * @return 如果在范围内则返回true
     */
    public static boolean inRange(Number value, Number min, Number max) {
        if (value == null || min == null || max == null) {
            return false;
        }
        return compareNumbers(value, min) >= 0 && compareNumbers(value, max) <= 0;
    }

    /**
     * 检查数值是否不在范围内
     *
     * @param value 要检查的数值
     * @param min   最小值
     * @param max   最大值
     * @return 如果不在范围内则返回true
     */
    public static boolean notInRange(Number value, Number min, Number max) {
        return !inRange(value, min, max);
    }

// ================ 数组/集合工具 ================

    /**
     * 检查数组是否包含元素
     *
     * @param array   数组
     * @param element 元素
     * @param <T>     元素类型
     * @return 如果包含则返回true
     */
    public static <T> boolean contains(T[] array, T element) {
        if (array == null || element == null) {
            return false;
        }
        for (T item : array) {
            if (element.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查数组是否不包含元素
     *
     * @param array   数组
     * @param element 元素
     * @param <T>     元素类型
     * @return 如果不包含则返回true
     */
    public static <T> boolean notContains(T[] array, T element) {
        return !contains(array, element);
    }

    /**
     * 检查Map是否包含键
     *
     * @param map Map
     * @param key 键
     * @return 如果包含键则返回true
     */
    public static boolean containsKey(Map<?, ?> map, Object key) {
        return map != null && map.containsKey(key);
    }

    /**
     * 检查Map是否不包含键
     *
     * @param map Map
     * @param key 键
     * @return 如果不包含键则返回true
     */
    public static boolean notContainsKey(Map<?, ?> map, Object key) {
        return !containsKey(map, key);
    }

    /**
     * 检查Map是否包含值
     *
     * @param map   Map
     * @param value 值
     * @return 如果包含值则返回true
     */
    public static boolean containsValue(Map<?, ?> map, Object value) {
        return map != null && map.containsValue(value);
    }

    /**
     * 检查Map是否不包含值
     *
     * @param map   Map
     * @param value 值
     * @return 如果不包含值则返回true
     */
    public static boolean notContainsValue(Map<?, ?> map, Object value) {
        return !containsValue(map, value);
    }


}
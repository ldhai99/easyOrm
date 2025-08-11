package io.github.ldhai99.easyOrm.tools;

import io.github.ldhai99.easyOrm.Lambda.PropertyGetter;
import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

import java.util.HashMap;
import java.util.Map;

public class ChainMap extends HashMap<String, Object> {

    /**
     * 创建一个空的 ChainMap
     */
    public static ChainMap of() {
        return new ChainMap();
    }

    /**
     * 从现有 Map 创建 ChainMap
     */
    public static ChainMap of(Map<String, Object> map) {
        ChainMap result = new ChainMap();
        result.putAll(map);
        return result;
    }

    /**
     * 从单个键值对创建 ChainMap
     */
    public static ChainMap of(String key, Object value) {
        ChainMap result = new ChainMap();
        result.put(key, value);
        return result;
    }

    /**
     * 从 Lambda 属性引用创建 ChainMap
     */
    public static <E> ChainMap of(PropertyGetter<E> getter, Object value) {
        ChainMap result = new ChainMap();
        result.put(FieldResolver.field(getter), value);
        return result;
    }

    // ===== 链式方法 =====

    /**
     * 链式 clear
     */
    public ChainMap reset() {
        super.clear();
        return this;
    }

    /**
     * 链式 put
     */
    public ChainMap set(String key, Object value) {
        put(key, value);
        return this;
    }

    /**
     * 支持 Lambda 属性引用的 set
     */
    public <E> ChainMap set(PropertyGetter<E> getter, Object value) {
        return set(FieldResolver.field(getter), value);
    }

    /**
     * 链式 remove（推荐命名）
     */
    public ChainMap remove(String key) {
        super.remove(key);
        return this;
    }

    /**
     * delete 作为 remove 的别名
     */
    public ChainMap delete(String key) {
        return remove(key);
    }

    /**
     * 链式 putAll
     */
    public ChainMap addAll(Map<? extends String, ?> m) {
        super.putAll(m);
        return this;
    }

    // 私有构造函数（可选）
    private ChainMap() {
        super();
    }

    // 测试
    public static void main(String[] args) {
       HashMap map= ChainMap.of("name", "Tom")
                .set("age", 18)
                .addAll(ChainMap.of("city", "Beijing"))
                .delete("age")
                //.reset()
                .set("ok", true);

        System.out.println(map); // {ok=true}
    }
}
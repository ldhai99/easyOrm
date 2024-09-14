package io.github.ldhai99.easyOrm.tools;

import java.util.HashMap;
import java.util.Map;

public class ChainMap extends HashMap<String, Object> {

    /**
     * 静态工厂方法用于从空 Map 创建 ChainMap 的实例
     */
    public static ChainMap of() {
        ChainMap  ChainMap = new ChainMap();
        return ChainMap;
    }

    /**
     * 静态工厂方法用于从给定的 Map 创建 ChainMap 的实例
     */
    public static ChainMap  of(Map map) {
        ChainMap ChainMap = new ChainMap();
        ChainMap.putAll(map);
        return ChainMap;
    }

    /**
     * 静态工厂方法用于从单个键值对创建 ChainMap 的实例
     */
    public static  ChainMap  of(String key, Object value) {
        ChainMap  ChainMap = new ChainMap();
        ChainMap.put(key, value);
        return ChainMap;
    }


    /**
     * 链式 clear 方法
     */
    @Override
    public void clear() {
        super.clear();
        // clear 方法没有返回值
    }

    /**
     * 链式 set 方法的链式调用版本
     */

    public ChainMap  set(String key, Object value) {
        //存在先删除
        if(this.containsKey(key))
            this.remove(key);
        put(key, value);
        return this;
    }
    /**
     * 链式 remove 方法的链式调用版本
     */
    public ChainMap  delete(String key) {
        remove(key);
        return this;
    }

    // 构造函数私有化，因为我们将使用静态工厂方法来创建实例
    private ChainMap() {
        super();
    }

    public static void main(String[] args) {
        // 从空 Map 创建
        ChainMap emptyMap = ChainMap.of()
                .set("one", 1)
                .set("two", 2);

        // 从已存在的 Map 创建
        Map<String, Integer> existingMap = new HashMap<>();
        existingMap.put("three", 3);
        existingMap.put("four", 4);
        ChainMap fromExistingMap = ChainMap.of(existingMap)
                .set("five", 5).set("1",1);

        // 从单个键值对创建
        ChainMap singleEntry = ChainMap.of("six", 6)
                .set("seven", 7);

        System.out.println(emptyMap); // 输出：{one=1, two=2}
        System.out.println(fromExistingMap); // 输出：{three=3, four=4, five=5}
        System.out.println(singleEntry); // 输出：{six=6, seven=7}
    }
}
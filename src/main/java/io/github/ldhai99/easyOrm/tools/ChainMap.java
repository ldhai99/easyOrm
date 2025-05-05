package io.github.ldhai99.easyOrm.tools;

import java.util.HashMap;
import java.util.Map;

public class ChainMap extends HashMap<String, Object> {

    /**
     * 静态工厂方法用于从空 Map 创建 ChainMap 的实例
     * 该方法提供了一种便捷的方式来初始化一个空的 ChainMap 对象
     * @return 返回一个新的空 ChainMap 实例
     */
    public static ChainMap of() {
        ChainMap  ChainMap = new ChainMap();
        return ChainMap;
    }

    /**
     * 静态工厂方法用于从给定的 Map 创建 ChainMap 的实例
     * 此方法允许用户将一个现有的 Map 对象作为输入，
     * 并返回一个包含该 Map 所有映射的 ChainMap 实例
     *
     * @param map 用于初始化 ChainMap 的 Map 对象
     * @return 返回一个新创建的 ChainMap 实例
     */
    public static ChainMap  of(Map map) {
        ChainMap ChainMap = new ChainMap();
        ChainMap.putAll(map);
        return ChainMap;
    }

    /**
     * 静态工厂方法用于从单个键值对创建 ChainMap 的实例
     * 这个方法简化了 ChainMap 实例的创建过程，当只需要添加一个键值对时，可以不必直接使用构造函数
     *
     * @param key 键值对中的键
     * @param value 键值对中的值
     * @return 返回包含指定键值对的新 ChainMap 实例
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
     * 提供链式调用的set方法，用于在Map中设置键值对
     * 如果键已经存在，则先删除旧的键值对，再添加新的键值对
     * 这种设计允许在单个调用中同时完成删除旧值和设置新值的操作，提高了代码的可读性和效率
     *
     * @param key 要设置的键
     * @param value 要设置的值
     * @return 返回ChainMap对象本身，以支持链式调用
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
     * @param key
     * @return
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
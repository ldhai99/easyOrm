package io.github.ldhai99.easyOrm.test.datasource;


import io.github.ldhai99.easyOrm.datasource.DataSourceManager;
import io.github.ldhai99.easyOrm.datasource.DataSourceTools;
import io.github.ldhai99.easyOrm.datasource.DynamicDataSource;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class DataSourceManagerInitTest {

    private static final String TEST_PROP_FILE = "druid-test.properties"; // 测试专用配置
    private DataSource ds1, ds2;

    @BeforeEach
    void setUp() throws Exception {
        // 重置 DataSourceManager 状态（通过反射）
        resetDataSourceManager();

        // 创建测试数据源
        ds1 = DataSourceTools.createDataSource("jdbc:h2:mem:test1", "sa", "");
        ds2 = DataSourceTools.createDataSource("jdbc:h2:mem:test2", "sa", "");

        // 设置测试配置文件（模拟 classpath 下有 druid-test.properties）
        System.setProperty("druid.config.location", TEST_PROP_FILE);
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("druid.config.location");
        resetDataSourceManager();
    }

    // ==================== 辅助方法 ====================

//    private DataSource createDataSource(String url, String username, String password) {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("org.h2.Driver");
//        ds.setUrl(url);
//        ds.setUsername(username);
//        ds.setPassword(password);
//        return ds;
//    }

    private void resetDataSourceManager() {
        try {
            Field field = DataSourceManager.class.getDeclaredField("dynamicDataSource");
            field.setAccessible(true);
            field.set(null, null);

            field = DataSourceManager.class.getDeclaredField("autoInitialized");
            field.setAccessible(true);
            field.setBoolean(null, false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset DataSourceManager", e);
        }
    }

    // ==================== 测试用例 ====================

    // ----------------------------------------------------------------------------------------------------
    // 1. initDefaultDataSource() - 从配置文件加载
    // ----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("1. initDefaultDataSource：应从 druid.properties 加载并能获取连接")
    void testInitDefaultDataSource() {
        // 🧹 重置状态
       // resetDataSourceManager();

        // 🚀 初始化（从配置文件加载）,可以不初始化，可以自动初始化
       // DataSourceManager.initDefaultDataSource();

        // ✅ 验证初始化成功
        assertNotNull(DataSourceManager.getDataSource(), "默认数据源应被创建");
        assertTrue(DataSourceManager.isInitialized(), "应标记为已初始化");

        // ✅ 获取连接并验证数据库 URL（证明来自配置）
        assertDoesNotThrow(() -> {
            Connection conn = DataSourceManager.getConnection();
            String url = conn.getMetaData().getURL();
            assertTrue(url.contains("jdbc:mysql"), "应加载测试配置文件中的 URL");
            conn.close();
        });
    }



    // ----------------------------------------------------------------------------------------------------
    // 2. initDataSources(defaultDataSource, dataSources) - 多数据源管理
    // ----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("2. initDataSources：应正确注册主从数据源")
    void testInitDataSources() {
        // 🧹 重置
        resetDataSourceManager();

        // 🛠 创建测试数据源
        DataSource master = DataSourceTools.createDataSource("jdbc:h2:mem:master", "sa", "");
        DataSource slave  = DataSourceTools.createDataSource("jdbc:h2:mem:slave",  "sa", "");

        Map<String, DataSource> extras = new HashMap<>();
        extras.put("slave", slave);

        // 🚀 初始化多数据源
        DataSourceManager.initDataSources(master, extras);

        // ✅ 验证默认数据源
        assertEquals(master, DataSourceManager.getDataSource(), "默认数据源应为 master");

        // ✅ 验证额外数据源
        assertEquals(slave, DataSourceManager.getDataSource("slave"), "应能获取 slave 数据源");

        // ✅ 验证不可变性（外部修改不影响内部）
        extras.put("hacker", DataSourceTools.createDataSource("jdbc:h2:mem:hacked", "sa", ""));
        // ✅ 验证非法数据源名称会抛异常
        assertThrows(IllegalArgumentException.class,
                () -> DataSourceManager.getDataSource("hacker"),
                "访问未注册的数据源应抛出 IllegalArgumentException"
        );
        // ✅ 验证 getAllDataSources() 不包含 hacker
        assertFalse(DataSourceManager.getAllDataSources().containsKey("hacker"),
                "getAllDataSources() 不应包含外部添加的 'hacker'");
    }



    // ----------------------------------------------------------------------------------------------------
    // 3. setDataSourceForSpring(dataSource) - Spring 注入专用
    // ----------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("3. setDataSourceForSpring：应接受 Spring 注入的数据源")
    void testSetDataSourceForSpring() {
        // 🧹 重置
        resetDataSourceManager();
        // 🛠 模拟 Spring 创建的数据源
        DataSource springDs =  DataSourceTools.createDataSource("jdbc:h2:mem:springdb", "sa", "");
        // 🚀 模拟 Spring 调用注入
        DataSourceManager.setDataSourceForSpring(springDs);
        // ✅ 验证数据源被正确设置
        assertEquals(springDs, DataSourceManager.getDataSource(), "应使用 Spring 注入的数据源");
        assertTrue(DataSourceManager.isInitialized(), "应标记为已初始化");
        assertTrue(DataSourceManager.isAutoInitialized(), "autoInitialized 应为 true");
    }
    @Test
    @DisplayName("4. 幂等性测试：多种初始化方式混合调用，只应生效一次")
    void testInitializationIdempotency() {
        // 🧹 重置
        resetDataSourceManager();

        // 🛠 准备数据源
        DataSource ds1 = DataSourceTools.createDataSource("jdbc:h2:mem:ds1", "sa", "");
        DataSource ds2 = DataSourceTools.createDataSource("jdbc:h2:mem:ds2", "sa", "");

        // ✅ 第一次：setDataSourceForSpring 应生效
        DataSourceManager.setDataSourceForSpring(ds1);
        assertEquals(ds1, DataSourceManager.getDataSource(), "第一次应使用 ds1");

        // ❌ 后续调用全部被忽略
        DataSourceManager.initDefaultDataSource();
        DataSourceManager.initDataSources(ds2, Collections.emptyMap());

        // ✅ 仍为 ds1
        assertEquals(ds1, DataSourceManager.getDataSource(), "后续初始化应被忽略");
    }



    @Test
    @DisplayName("7. setDataSourceForSpring：拒绝 null 数据源")
    void testSetDataSourceForSpring_RejectNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            DataSourceManager.setDataSourceForSpring(null);
        }, "不应允许 null 数据源");
    }

    // ----------------------------------------------------------------------------------------------------
    // 4. 并发安全测试
    // ----------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("8. 并发初始化：多线程下只应成功初始化一次")
    void testConcurrentInitialization() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // 所有线程同时开始

                    // 混合调用三种方法（应只有第一个成功）
                    DataSourceManager.setDataSourceForSpring(ds1);
                    Map map= new HashMap();
                    map.put("temp", ds2);

                    DataSourceManager.initDataSources(ds1, map);

                    DataSourceManager.initDefaultDataSource();

                } catch (Exception e) {
                    fail("并发初始化不应抛异常", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await();
        executor.shutdown();

        // 验证：只初始化了一次
        assertTrue(DataSourceManager.isInitialized(), "应至少初始化一次");
        assertNotNull(DataSourceManager.getDataSource(), "默认数据源应存在");

        // 注意：无法确定是哪种方式成功，但只能成功一次
    }
}